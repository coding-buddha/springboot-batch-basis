DROP TABLE books IF EXISTS;

-- PK, 등록번호, 서명, 발행자, 발행년, 저자, 청구기호, ISBN, 관리구분, 자료실, PRICE
CREATE TABLE books
(
    id                VARCHAR(50) NOT NULL PRIMARY KEY,
    title             VARCHAR(200),
    publisher         VARCHAR(255),
    year              BIGINT,
    author            VARCHAR(255),
    call_name         VARCHAR(255),
    isbn              VARCHAR(255),
    library           VARCHAR(255),
    library_reference VARCHAR(255),
    price             BIGINT
);