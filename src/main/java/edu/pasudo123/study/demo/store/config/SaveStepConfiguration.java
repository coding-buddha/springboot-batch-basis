package edu.pasudo123.study.demo.store.config;

import edu.pasudo123.study.demo.store.model.Store;
import edu.pasudo123.study.demo.store.model.StoreItem;
import edu.pasudo123.study.demo.store.processor.StoreItemSaveProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
public class SaveStepConfiguration {

    private final EntityManagerFactory entityManagerFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static final String SAVE_STEP = "saveStep";
    private static final int CHUNK_SIZE = 10;

    @Bean
    public FlatFileItemReader<StoreItem> saveJobReader() {
        // csv file 를 읽어들인다.
        return new FlatFileItemReaderBuilder<StoreItem>()
                .name("storeItemReader")
                .resource(new ClassPathResource("csv/store_list.csv"))      // 어디 파일 읽을 것인지
                .delimited()                                                // 기본 구분자는 콤마   
                .names("no", "name", "address", "phoneNumber")              // csv 파일과 매핑되는 클래스 필드 작성
                .targetType(StoreItem.class)                                // 타겟 class type
                .build();
    }

    @Bean
    public StoreItemSaveProcessor saveJobProcessor() {
        return new StoreItemSaveProcessor();
    }

    /**
     * 저장을 수행.
     */
    @Bean
    public JpaItemWriter<Store> jpaItemWriter() {
        // jpa 를 사용하기 때문에, EntityManagerFactory 를 할당해준다.
        return new JpaItemWriterBuilder<Store>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();
    }

    /**
     * csv 파일을 읽어서 저장을 수행하는 save step
     */
    @Bean
    @Qualifier("saveStep")
    public Step saveStep() {
        return stepBuilderFactory.get(SAVE_STEP)
                .<StoreItem, Store> chunk(CHUNK_SIZE)
                .reader(saveJobReader())
                .processor(saveJobProcessor())
                .writer(jpaItemWriter())
                .build();
    }
}
