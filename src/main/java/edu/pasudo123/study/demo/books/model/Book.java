package edu.pasudo123.study.demo.books.model;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Book {
    private String regId;
    private String title;
    private String publisher;
    private Long year;
    private String author;
    private String callName;
    private String isbn;
    private String library;
    private String libraryReference;
    private Long price;

    @Builder
    public Book(String regId, String title, String publisher, Long year, String author,
                String callName, String isbn, String library, String libraryReference, Long price) {
        this.regId = regId;
        this.title = title;
        this.publisher = publisher;
        this.year = year;
        this.author = author;
        this.callName = callName;
        this.isbn = isbn;
        this.library = library;
        this.libraryReference = libraryReference;
        this.price = price;
    }
}
