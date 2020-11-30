-- h2 랑 DROP TABLE 시에 순서가 다르다.
DROP TABLE IF EXISTS store;
DROP TABLE IF EXISTS member;

CREATE TABLE store
(
    id                  BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    no                  BIGINT NOT NULL,
    name                VARCHAR(50) NOT NULL,
    address             VARCHAR(100) NOT NULL,
    phone_number        VARCHAR(100)
);

CREATE TABLE member
(
    id                  BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name                VARCHAR(50) NOT NULL,
    status              ENUM('CREATE', 'UPDATE' ,'DELETE') NOT NULL
)