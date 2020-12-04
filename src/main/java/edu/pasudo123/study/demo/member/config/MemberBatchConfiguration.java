package edu.pasudo123.study.demo.member.config;

import edu.pasudo123.study.demo.member.mapper.MemberRowMapper;
import edu.pasudo123.study.demo.member.model.Member;
import edu.pasudo123.study.demo.member.model.MemberItem;
import edu.pasudo123.study.demo.member.notification.MemberJobListener;
import edu.pasudo123.study.demo.member.notification.MemberUpdateStepListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(
        value = "batch.model.members.enabled",
        havingValue = "true",
        matchIfMissing = false
)
public class MemberBatchConfiguration {

    private final EntityManagerFactory entityManagerFactory;
    private final DataSource dataSource;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static final int CHUNK_SIZE = 10;

    @Qualifier("middleSuccessStep")
    private final Step middleSuccessStep;

    @Qualifier("middleFailedStep")
    private final Step middleFailedStep;

    @Bean
    public Job memberJob() {
        return jobBuilderFactory.get("memberJob")
                .preventRestart()
                .incrementer(new RunIdIncrementer())    // 동일 JobParameter 에 대해서 다시 시작하도록 설정하기 위함
                .listener(new MemberJobListener())
                .start(clearDbStep())
                .next(csvToDbStep())

                /** changeUpdateStep() 을 수행하고, 종료코드가 COMPLETED 면 changeDeleteStep() 로 이동 **/
                /** conditional step 을 수행할 시에, to() 메소드 인자값이 동일한 step 이면 올바르게 작동되지 않음 (SimpleFlow.java 의 nextState() 참고) **/                .next(changeUpdateStep())
                    .on("COMPLETED").to(middleSuccessStep)
                    .next(changeDeleteStep())
                    .next(changeCompletedStep())

                /** changeUpdateStep() 을 수행하고, 종료코드가 FAILED 면 changeUpdateFailedStep() 로 이동 **/
                .from(changeUpdateStep())
                    .on("FAILED").to(middleFailedStep)
                    .next(changeUpdateFailedStep())
                    .next(changeCompletedStep())

                .end()
                .build();
    }

    /**
     * csvFile
     * ======================================
     * 1. reader / processor / writer
     */
    @Bean
    public FlatFileItemReader<MemberItem> csvFileReader() {
        return new FlatFileItemReaderBuilder<MemberItem>()
                .name("csvFileReader")
                .resource(new ClassPathResource("csv/member_list.csv"))
                .delimited()
                .names("name", "status")
                .targetType(MemberItem.class)
                .build();
    }

    @Bean
    public ItemProcessor<MemberItem, Member> itemToMemberProcessor() {
        return MemberItem::toMemberEntity;
    }

    @Bean
    public JpaItemWriter<Member> csvFileWriter() {
        return new JpaItemWriterBuilder<Member>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)   // 신규 엔티티 생성시 true 는 성능향상에 이점이 존재한다.
                .build();
    }

    @Bean
    @Qualifier("csvToDbStep")
    public Step csvToDbStep() {

        return stepBuilderFactory.get("csvToDbStep")
                .<MemberItem, Member> chunk(CHUNK_SIZE)
                .reader(csvFileReader())
                .processor(itemToMemberProcessor())
                .writer(csvFileWriter())
                .build();
    }

    /**
     * 나머지 writer
     */
    @Bean
    public JpaItemWriter<Member> jpaItemWriter() {
        return new JpaItemWriterBuilder<Member>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(false)  // update 쿼리가 필요하기 때문에 select 이후 더티체킹 하도록 한다.
                .build();
    }

    /**
     * changeUpdate
     * ======================================
     * 2. reader / writer
     */
    @Bean
    public JdbcCursorItemReader<Member> changeUpdateReader() {
        //noinspection DuplicatedCode
        return new JdbcCursorItemReaderBuilder<Member>()
                .dataSource(dataSource)
                .name("changeUpdateReader")
                .sql("SELECT id, name, status FROM member WHERE id")
                .rowMapper(new MemberRowMapper())
                .fetchSize(CHUNK_SIZE)
                .driverSupportsAbsolute(true)
                .build();
    }

    @Bean
    public ItemProcessor<Member, Member> changeUpdateProcessor() {
        return member -> {
            log.info("======> update 상태로 변경합니다.");
            member.changeStatusToUpdate();
            // step 의 조건절을 테스트하기 위함
//            member.doForceError();
            return member;
        };
    }

    @Bean
    @Qualifier("changeUpdateStep")
    public Step changeUpdateStep() {
        return stepBuilderFactory.get("changeUpdateStep")
                .<Member, Member> chunk(CHUNK_SIZE)
                .reader(changeUpdateReader())
                .processor(changeUpdateProcessor())
                .writer(jpaItemWriter())
                .listener(new MemberUpdateStepListener())
                .build();
    }

    /**
     * updateToFailed
     * ======================================
     * 2-5. reader / writer
     */
    @Bean
    public JdbcCursorItemReader<Member> updateToFailedReader() {
        //noinspection DuplicatedCode
        return new JdbcCursorItemReaderBuilder<Member>()
                .dataSource(dataSource)
                .name("updateToFailedReader")
                .sql("SELECT id, name, status FROM member")
                .rowMapper(new MemberRowMapper())
                .fetchSize(CHUNK_SIZE)
                .driverSupportsAbsolute(true)
                .build();
    }

    @Bean
    public ItemProcessor<Member, Member> changeUpdateFailedProcessor() {
        return member -> {
            log.info("======> update failed 상태로 변경합니다.");
            member.changeStatusToUpdateFailed();
            return member;
        };
    }

    @Bean
    @Qualifier("changeUpdateFailedStep")
    public Step changeUpdateFailedStep() {
        return stepBuilderFactory.get("changeUpdateFailedStep")
                .<Member, Member> chunk(CHUNK_SIZE)
                .reader(updateToFailedReader())
                .processor(changeUpdateFailedProcessor())
                .writer(jpaItemWriter())
                .build();
    }

    /**
     * changeDelete
     * ======================================
     * 3. reader / writer
     */
    @Bean
    public JdbcCursorItemReader<Member> changeDeleteReader() {
        //noinspection DuplicatedCode
        return new JdbcCursorItemReaderBuilder<Member>()
                .dataSource(dataSource)
                .name("changeDeleteReader")
                .sql("SELECT id, name, status FROM member")
                .rowMapper(new MemberRowMapper())
                .fetchSize(CHUNK_SIZE)
                .driverSupportsAbsolute(true)
                .build();
    }

    @Bean
    public ItemProcessor<Member, Member> changeDeleteProcessor() {
        return member -> {
            log.info("======> delete 상태로 변경합니다.");
            member.changeStatusToDelete();
            return member;
        };
    }

    @Bean
    @Qualifier("changeDeleteStep")
    public Step changeDeleteStep() {
        return stepBuilderFactory.get("changeDeleteStep")
                .<Member, Member> chunk(CHUNK_SIZE)
                .reader(changeDeleteReader())
                .processor(changeDeleteProcessor())
                .writer(jpaItemWriter())
                .build();
    }

    /**
     * deleteToFailed
     * ======================================
     * 3-5. reader / writer
     */
    @Bean
    public JdbcCursorItemReader<Member> deleteToFailedReader() {
        //noinspection DuplicatedCode
        return new JdbcCursorItemReaderBuilder<Member>()
                .dataSource(dataSource)
                .name("deleteToFailedReader")
                .sql("SELECT id, name, status FROM member")
                .rowMapper(new MemberRowMapper())
                .fetchSize(CHUNK_SIZE)
                .driverSupportsAbsolute(true)
                .build();
    }

    @Bean
    public ItemProcessor<Member, Member> changeDeleteFailedProcessor() {
        return member -> {
            log.info("======> delete failed 상태로 변경합니다.");
            member.changeStatusToDeleteFailed();
            return member;
        };
    }

    @Bean
    @Qualifier("changeDeleteFailedStep")
    public Step changeDeleteFailedStep() {
        return stepBuilderFactory.get("changeDeleteFailedStep")
                .<Member, Member> chunk(CHUNK_SIZE)
                .reader(deleteToFailedReader())
                .processor(changeDeleteFailedProcessor())
                .writer(jpaItemWriter())
                .build();
    }

    /**
     * dbClear
     * ======================================
     * 4. reader / writer
     */
    @Bean
    public JdbcCursorItemReader<Long> dbClearReader() {
        return new JdbcCursorItemReaderBuilder<Long>()
                .dataSource(dataSource)
                .name("dbClearReader")
                .sql("SELECT id FROM member")
                .rowMapper((rs, rowNum) -> rs.getLong("id"))
                .fetchSize(CHUNK_SIZE)
                .driverSupportsAbsolute(true)
                .build();
    }

    @Bean
    public ItemWriter<Long> dbClearWriter() {
        return ids -> {
            final EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);

            if(entityManager == null) {
                throw new DataAccessResourceFailureException("Unable to obtain a transactional EntityManager");
            }

            Query query = entityManager.createQuery("DELETE FROM Member m WHERE m.id IN (:ids)");
            query.setParameter("ids", ids);
            query.executeUpdate();
            entityManager.flush();
        };
    }

    @Bean
    @Qualifier("clearDbStep")
    public Step clearDbStep() {
        return stepBuilderFactory.get("clearDbStep")
                .<Long, Long> chunk(CHUNK_SIZE)
                .reader(dbClearReader())
                .writer(dbClearWriter())
                .build();
    }

    /**
     * completed
     * ======================================
     * 5. reader / writer
     */
    @Bean
    public JdbcCursorItemReader<Member> completedReader() {
        //noinspection DuplicatedCode
        return new JdbcCursorItemReaderBuilder<Member>()
                .dataSource(dataSource)
                .name("completedReader")
                .sql("SELECT id, name, status FROM member")
                .rowMapper(new MemberRowMapper())
                .fetchSize(CHUNK_SIZE)
                .driverSupportsAbsolute(true)
                .build();
    }

    @Bean
    public ItemProcessor<Member, Member> completedProcessor() {
        return member -> {
            log.info("======> completed 상태로 변경합니다.");
            member.changeToCompleted();
            return member;
        };
    }

    @Bean
    @Qualifier("changeCompletedStep")
    public Step changeCompletedStep() {
        return stepBuilderFactory.get("changeCompletedStep")
                .<Member, Member> chunk(CHUNK_SIZE)
                .reader(completedReader())
                .processor(completedProcessor())
                .writer(jpaItemWriter())
                .build();
    }
}
