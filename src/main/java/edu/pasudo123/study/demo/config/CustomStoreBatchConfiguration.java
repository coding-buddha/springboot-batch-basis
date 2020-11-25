package edu.pasudo123.study.demo.config;

import edu.pasudo123.study.demo.store.model.Store;
import edu.pasudo123.study.demo.store.processor.StoreItemProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

/**
 * 프로파일 구분에 따라 configuration 을 수행할 수 있도록 하였다.
 */
@Profile("profile-store")
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class CustomStoreBatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * csv file reader 세팅
     * 해당 객체는 job 에 종속된다.
     */
    @Bean
    public FlatFileItemReader<Store> reader() {
        return new FlatFileItemReaderBuilder<Store>()
                .name("storeItemReader")
                .resource(new ClassPathResource("csv/store_list.csv"))
                .delimited()
                .names("no, name, address, phone_number")
                .targetType(Store.class)
                .build();
    }

    @Bean
    public StoreItemProcessor processor() {
        return new StoreItemProcessor();
    }
}
