package edu.pasudo123.study.demo.store.config;

import edu.pasudo123.study.demo.notification.CustomNotificationListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 프로파일 구분에 따라 configuration 을 수행할 수 있도록 하였다.
 */
@Configuration
@RequiredArgsConstructor
public class StoreBatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    @Qualifier("saveStep") private final Step saveStep;
    @Qualifier("deleteStep") private final Step deleteStep;

    @Bean
    public Job storeJob(CustomNotificationListener listener) {
        return jobBuilderFactory.get("storeJob")
                .incrementer(new RunIdIncrementer())    // 동일 파라미터 재 실행하고 싶은 경우 : RunIdIncrementer 이 제공
                .listener(listener)
                .start(saveStep)
                .next(deleteStep)
                .build();
    }
}
