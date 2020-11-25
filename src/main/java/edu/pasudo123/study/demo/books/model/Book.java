package edu.pasudo123.study.demo.books.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Book {
    private final String regId;
    private final String title;
    private final String publisher;
    private final String year;
    private final String author;
    private final String callName;
    private final String isbn;
    private final String library;
    private final String libraryReference;
    private final String price;

    public Book(String regId, String title, String publisher, String year, String author, String callName, String isbn, String library, String libraryReference, String price) {
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
