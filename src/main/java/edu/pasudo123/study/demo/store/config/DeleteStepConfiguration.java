package edu.pasudo123.study.demo.store.config;

import edu.pasudo123.study.demo.store.model.Store;
import edu.pasudo123.study.demo.store.processor.StoreItemDeleteProcessor;
import edu.pasudo123.study.demo.store.writer.CustomStoreWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
public class DeleteStepConfiguration {

    private final EntityManagerFactory entityManagerFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static final String DELETE_STEP = "deleteStep";
    private static final int CHUNK_SIZE = 20;

    /**
     * 특정한 조건을 기준으로 page_size = 20 만큼 SELECT 수행
     * @return
     */
    @Bean
    public JpaPagingItemReader<Store> deleteJobReader() {
        return new JpaPagingItemReaderBuilder<Store>()
                .name("storeItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(CHUNK_SIZE)                                   // 조회개수
                .queryString("SELECT item FROM Store item")             // 조건절 설정
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
                .processor(deleteJobProcessor())
                .writer(deleteJobWriter())
                .build();
    }
}
