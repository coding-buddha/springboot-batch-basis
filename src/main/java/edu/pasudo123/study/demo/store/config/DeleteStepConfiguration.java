package edu.pasudo123.study.demo.store.config;

import edu.pasudo123.study.demo.store.model.Store;
import edu.pasudo123.study.demo.store.processor.StoreItemDeleteProcessor;
import edu.pasudo123.study.demo.store.writer.CustomStoreWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
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

    /**
     * 특정한 조건을 기준으로 page_size 만큼 SELECT 수행
     * TODO 데이터를 삭제처리하는 조건으로 읽어들이면 limit ?, ? 로 나가는데, 데이터 삭제가 일부 누락된다.
     * @return
     */
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
    public JdbcCursorItemReader<Store> deleteJobReader() {
        return new JdbcCursorItemReaderBuilder<Store>()
                .dataSource(dataSource)
                .name("storeItemReader")
                .sql("SELECT id, no, name, address, phone_number FROM store")
                .rowMapper(new RowMapper<Store>(){
                    @Override
                    public Store mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Store store = Store.builder()
                                .no(rs.getLong("no"))
                                .name(rs.getString("name"))
                                .address(rs.getString("address"))
                                .phoneNumber(rs.getString("phone_number"))
                                .build();
                        store.updateId(rs.getLong("id"));
                        return store;
                    }
                })
                .fetchSize(CHUNK_SIZE)
                .driverSupportsAbsolute(true)
                .build();
    }

    @Bean
    public StoreItemDeleteProcessor deleteJobProcessor() {
        return new StoreItemDeleteProcessor();
    }

    @Bean
    public ItemWriter<Store> deleteJobWriter() {
        return new CustomStoreWriter(entityManagerFactory);
    }

    @Bean
    @Qualifier("deleteStep")
    public Step deleteStep() {
        return stepBuilderFactory.get(DELETE_STEP)
                .<Store, Store> chunk(CHUNK_SIZE)                       // 조회된 개수만큼 writer 를 수행.
                .reader(deleteJobReader())
                .processor(deleteJobProcessor())    // processor 는 필수가 아니다. 데이터에 대한 변경로직이 없다면 processor 제외가 가능
                .writer(deleteJobWriter())
                .build();
    }
}
