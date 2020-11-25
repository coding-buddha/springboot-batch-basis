package edu.pasudo123.study.demo.books.processor;

import edu.pasudo123.study.demo.books.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class BookItemProcessor implements ItemProcessor<Book, Book> {

    @Override
    public Book process(final Book bookItem) {
        // 데이터를 수집하고 변환을 수행.
        // 파이프라인 연결
        log.info("current : {}", bookItem);
        return bookItem;
    }
}
