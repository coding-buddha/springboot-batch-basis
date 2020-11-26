package edu.pasudo123.study.demo.store.config;

import edu.pasudo123.study.demo.notification.NotificationListener;
import edu.pasudo123.study.demo.store.model.Store;
import edu.pasudo123.study.demo.store.processor.StoreItemSaveProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private final JobBuilderFactory jobBuilderFactory;
    @Qualifier("saveStep") private final Step saveStep;

    @Bean
    public Job storeJob(NotificationListener listener) {
        return jobBuilderFactory.get("storeJob")
                .incrementer(new RunIdIncrementer())    // 동일 파라미터 재 실행하고 싶은 경우 : RunIdIncrementer 이 제공
                .listener(listener)
                .flow(saveStep)
                .end()
                .build();
    }


//    /**
//     * db 에 저장된 데이터 로우를 삭제하는 delete step
//     */
//    @Bean
//    public Step deleteStep() {
//        return stepBuilderFactory.get(DELETE_STEP)
//                .<Store, Store> chunk(CHUNK_SIZE)
//                .reader(saveJobReader)
//                .processor(saveJobProcessor)
//                .writer(jpaItemWriter())
//                .build();
//    }
//
//    @Bean
//    public JpaItemWriter<Store> jpaItemWriter() {
//        // jpa 를 사용하기 때문에, EntityManagerFactory 를 할당해준다.
//        return new JpaItemWriterBuilder<Store>()
//                .entityManagerFactory(entityManagerFactory)
//                .usePersist(true)
//                .build();
//    }
}
