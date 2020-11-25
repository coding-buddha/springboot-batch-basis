DROP TABLE books IF EXISTS;

-- PK, 등록번호, 서명, 발행자, 발행년, 저자, 청구기호, ISBN, 관리구분, 자료실, PRICE
CREATE TABLE books  (
    id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    reg_id VARCHAR(50) UNIQUE,
    title VARCHAR(200),
    publisher VARCHAR(50),
    year BIGINT,
    author VARCHAR(50),
    call_name VARCHAR(50),
    isbn VARCHAR(50),
    library VARCHAR(50),
    library_reference VARCHAR(50),
    price BIGINT
);