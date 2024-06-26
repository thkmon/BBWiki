use mysql;
create user hdwk@localhost identified by 'mypassword';
create user hdwk@'%' identified by 'mypassword';

create database hdwk default character set utf8;

grant all privileges ON hdwk.* TO hdwk@'%';
flush privileges;

--

USE hdwk;

CREATE TABLE BBWK_DOC (
  DOC_ID VARCHAR(50) PRIMARY KEY,
  TITLE VARCHAR(500) NOT NULL,
  SUMMARY VARCHAR(4000) NOT NULL,
  TEXT_ID VARCHAR(50) NOT NULL,
  REG_USER_ID VARCHAR(100) NOT NULL,
  REG_USER_NAME VARCHAR(100) NOT NULL,
  REG_TIME VARCHAR(100) NOT NULL,
  LAST_MOD_USER_ID VARCHAR(100),
  LAST_MOD_USER_NAME VARCHAR(100),
  LAST_MOD_TIME VARCHAR(100),
  IS_DELETE VARCHAR(1) NOT NULL);

ALTER TABLE BBWK_DOC ADD COLUMN TEXT_LEN VARCHAR(100);
-- [TEXT_LEN 컬럼 일괄 업데이트]
-- UPDATE BBWK_DOC D SET TEXT_LEN = (SELECT CHAR_LENGTH(T.TEXT_OBJ) FROM BBWK_TEXT T WHERE T.TEXT_ID = D.TEXT_ID);
CREATE INDEX IDX_BBWK_DOC_TEXT_LEN ON BBWK_DOC (TEXT_LEN);

CREATE TABLE BBWK_TEXT (
  TEXT_ID VARCHAR(50) PRIMARY KEY,
  REG_TIME VARCHAR(100) NOT NULL,
  TEXT_OBJ TEXT NOT NULL,
  DOC_ID VARCHAR(50) NOT NULL);

CREATE TABLE BBWK_DOC_HISTORY (
  HISTORY_ID VARCHAR(50) PRIMARY KEY,
  HISTORY_TIME VARCHAR(100) NOT NULL,
  DOC_ID VARCHAR(50) NOT NULL,
  USER_ID VARCHAR(100) NOT NULL,
  USER_NAME VARCHAR(100) NOT NULL,
  BEFORE_TEXT_ID VARCHAR(50) NOT NULL,
  AFTER_TEXT_ID VARCHAR(50) NOT NULL,
  MOD_COUNT VARCHAR(50));

-- CREATE INDEX <인덱스명> ON <테이블명> ( 칼럼명1, 칼럼명2, ... );
CREATE INDEX IDX_BBWK_DOC_HISTORY ON BBWK_DOC_HISTORY (DOC_ID);
