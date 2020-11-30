package edu.pasudo123.study.demo.store.config;

import edu.pasudo123.study.demo.store.model.Store;
import edu.pasudo123.study.demo.store.processor.StoreItemDeleteProcessor;
import edu.pasudo123.study.demo.store.writer.CustomStoreWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
@RequiredArgsConstructor
public class DeleteStepConfiguration {

    private final EntityManagerFactory entityManagerFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private static final String DELETE_STEP = "deleteStep";
    private static final int CHUNK_SIZE = 20;

//    @Bean
//    public JpaPagingItemReader<Store> deleteJobReader() {
//        return new JpaPagingItemReaderBuilder<Store>()
//                .name("storeItemReader")
//                .entityManagerFactory(entityManagerFactory)
//                .pageSize(CHUNK_SIZE)                                   // 조회개수
//                .queryString("SELECT item FROM Store item")             // 조건절 설정
//                .build();
//    }

    /**
     * 커서단위로 읽어들이고, 해당 정크사이즈만큼 writer 에서 하도록 한다.
     * @return
     */
    @Bean
    public JdbcCursorItemReader<Long> deleteJobReader() {
        return new JdbcCursorItemReaderBuilder<Long>()
                .dataSource(dataSource)
                .name("storeItemReader")
                .sql("SELECT id FROM store")
                .rowMapper((rs, rowNum) -> rs.getLong("id"))
                .fetchSize(CHUNK_SIZE)          // 데이터베이스에서 call 수행 시 반환 갯수 힌트 값
                .driverSupportsAbsolute(true)   // jdbc 드라이버가 ResultSet 의 강제이동을 지원하는지 여부, 대규모의 데이터 셋의 경우에 성능 향상을 위해 true 가 좋음.
                .build();
    }

    @Bean
    public StoreItemDeleteProcessor deleteJobProcessor() {
        return new StoreItemDeleteProcessor();
    }

    @Bean
    public ItemWriter<Long> deleteJobWriter() {
        return new CustomStoreWriter(entityManagerFactory);
    }

    @Bean
    @Qualifier("deleteStep")
    public Step deleteStep() {
        return stepBuilderFactory.get(DELETE_STEP)
                .<Long, Long> chunk(CHUNK_SIZE)                       // 조회된 개수만큼 writer 를 수행.
                .reader(deleteJobReader())
                .writer(deleteJobWriter())
                .build();
    }
}
