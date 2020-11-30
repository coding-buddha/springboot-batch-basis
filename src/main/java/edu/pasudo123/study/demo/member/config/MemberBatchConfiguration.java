package edu.pasudo123.study.demo.member.config;

import edu.pasudo123.study.demo.member.model.Member;
import edu.pasudo123.study.demo.member.model.MemberItem;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(
        value = "batch.model.members.enabled",
        havingValue = "true",
        matchIfMissing = false
)
public class MemberBatchConfiguration {

    private final EntityManagerFactory entityManagerFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job memberJob() {
        return jobBuilderFactory.get("memberJob")
                .start(csvToDbStep())
                .build();
    }

    @Bean
    public FlatFileItemReader<MemberItem> csvFileReader() {
        return new FlatFileItemReaderBuilder<MemberItem>()
                .name("memberItemReader")
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
    public JpaItemWriter<Member> jpaItemWriter() {
        return new JpaItemWriterBuilder<Member>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();
    }

    @Bean
    public Step csvToDbStep() {
        return stepBuilderFactory.get("csvToDbStep")
                .<MemberItem, Member> chunk(3)
                .reader(csvFileReader())
                .processor(itemToMemberProcessor())
                .writer(jpaItemWriter())
                .build();
    }


}
