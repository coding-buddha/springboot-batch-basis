package edu.pasudo123.study.demo.store.config;

import edu.pasudo123.study.demo.store.model.Store;
import edu.pasudo123.study.demo.store.processor.StoreItemSaveProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * TODO 사용할경우에 lazy 하게 하도록 하고싶다면.. ?
 */
@Configuration
public class SaveJobConfiguration {

    @Bean
    public FlatFileItemReader<Store> saveJobReader() {
        // csv file 를 읽어들인다.
        return new FlatFileItemReaderBuilder<Store>()
                .name("storeItemReader")
                .resource(new ClassPathResource("csv/store_list.csv"))
                .delimited()
                .names("no, name, address, phone_number")
                .targetType(Store.class)
                .build();
    }

    @Bean
    public StoreItemSaveProcessor saveJobProcessor() {
        return new StoreItemSaveProcessor();
    }
}
