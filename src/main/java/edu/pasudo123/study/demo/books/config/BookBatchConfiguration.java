package edu.pasudo123.study.demo.books.config;

import edu.pasudo123.study.demo.books.model.Book;
import edu.pasudo123.study.demo.books.notification.CompleteNotificationListener;
import edu.pasudo123.study.demo.books.processor.BookItemProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Profile("profile-book")
@Configuration
@RequiredArgsConstructor
public class BookBatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /** ================ start : reader & processor & writer ================ **/
    /**
     * csv 파일을 읽어들이는 itemReader 를 만든다.
     * parsing 을 하면서, Book class 로 변환시킨다.
     */
    @Bean
    public FlatFileItemReader<Book> reader() {
        return new FlatFileItemReaderBuilder<Book>()
                .name("bookItemReader")
                .resource(new ClassPathResource("csv/book_list.csv"))
                .delimited().delimiter(",")
                .names("regId", "title", "publisher", "year", "author", "callName", "isbn", "library", "libraryReference", "price")
                .targetType(Book.class)
                .build();
    }

    @Bean
    public BookItemProcessor processor() {
        return new BookItemProcessor();
    }

    /**
     * itemWriter 를 만든다.
     */
    @Bean
    public JdbcBatchItemWriter<Book> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Book>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO books VALUES (:regId, :title, :publisher, :year, :author, :callName, :isbn, :library, :libraryReference, :price)")
                .dataSource(dataSource)
                .build();
    }
    /** end : reader & writer processor **/


    /** ================ start : jop-step ================ **/
    /**
     * job 을 정의한다.
     */
    @Bean
    public Job importUserJob(CompleteNotificationListener listener, Step firstStep){
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(firstStep)
                .end()
                .build();
    }

    @Bean
    public Step firstStep(JdbcBatchItemWriter<Book> writer) {
        return stepBuilderFactory.get("firstStep")
                .<Book, Book> chunk(100)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
    /** end : jop-step **/

}
