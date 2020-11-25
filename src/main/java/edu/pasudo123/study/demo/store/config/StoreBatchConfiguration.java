package edu.pasudo123.study.demo.store.config;

import edu.pasudo123.study.demo.books.notification.CompleteNotificationListener;
import edu.pasudo123.study.demo.store.model.Store;
import edu.pasudo123.study.demo.store.processor.StoreItemSaveProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.persistence.EntityManagerFactory;

/**
 * 프로파일 구분에 따라 configuration 을 수행할 수 있도록 하였다.
 */
@Profile("profile-store")
@Configuration
@RequiredArgsConstructor
public class StoreBatchConfiguration {

    // save job
    private final FlatFileItemReader<Store> saveJobReader;
    private final StoreItemSaveProcessor saveJobProcessor;

    // delete job

    // spring batch
    private final EntityManagerFactory entityManagerFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    // constants
    private static final String SAVE_STEP = "saveStep";
    private static final String DELETE_STEP = "deleteStep";
    private static final int CHUNK_SIZE = 10;

    @Bean
    public Job storeJob(CompleteNotificationListener listener) {
//        return jobBuilderFactory.get("")
        return null;
    }

    @Bean
    public Step saveStep() {
        return stepBuilderFactory.get(SAVE_STEP)
                .<Store, Store> chunk(CHUNK_SIZE)
                .reader(saveJobReader)
                .processor(saveJobProcessor)
                .writer(jpaItemWriter())
                .build();
    }

    @Bean
    public Step deleteStep() {
        return stepBuilderFactory.get(DELETE_STEP)
                .<Store, Store> chunk(CHUNK_SIZE)
                .reader(saveJobReader)
                .processor(saveJobProcessor)
                .writer(jpaItemWriter())
                .build();
    }

    @Bean
    public JpaItemWriter<Store> jpaItemWriter() {
        // jpa 를 사용하기 때문에, EntityManagerFactory 를 할당해준다.
        return new JpaItemWriterBuilder<Store>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();
    }
}
