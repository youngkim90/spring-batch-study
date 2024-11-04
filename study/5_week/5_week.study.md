# Spring Batch Study 5

참고: [DEVOCEAN KIDO님 SpringBatch 연재 5](https://devocean.sk.com/blog/techBoardDetail.do?ID=166867)

---

Spring Batch의 `JdbcPagingItemReader`와 `JdbcBatchItemWriter`로 DB 데이터를 읽고 쓰는 방법을 알아보자.

## 1. JdbcPagingItemReader/JdbcBatchItemWriter 개요

### JdbcPagingItemReader 개요

- Spring Batch에서 제공하는 `ItemReader` 인터페이스를 구현하는 클래스이다.
- **페이지 단위 데이터 읽기**: 데이터베이스에서 **페이지 단위**로 데이터를 읽어오는 기능을 제공한다.
- **대규모 데이터 처리 효율성**: 지정된 크기 만큼만 읽어와 메모리 사용량을 최소화하고, Chunk 단위로 커밋 간격을 설정하여 대규모 데이터를 **효율적**으로 처리할 수 있다.
- **페이지네이션 방식과 성능 최적화**: 각 페이지를 독립적으로 조회하는 페이지네이션 방식을 사용하여 커서 유지 부담을 줄이고 대용량 데이터를 **안정적**으로 처리할 수 있다.
- **SQL 쿼리 정의**: SQL 쿼리를 **직접 작성**하여 최적화된 데이터 읽기가 가능하다.
- **커서 제어**: 데이터베이스 **커서를 사용**하여 데이터 순회를 제어할 수 있다.

### JdbcPagingItemReader 주요 구성 요소

- **DataSource**: 데이터베이스 **연결 정보**를 설정한다.
- **SqlQuery**: **SelectClause**, **FromClause**, **WhereClause**, **SortKeys** 구성요소를 통해 데이터를 읽을 SQL 쿼리를 설정한다.
- **RowMapper**: SQL 쿼리의 각 결과 행을 **Item으로 변환**하는 역할을 한다.
- **PageSize**: Chunk와는 별도로 **페이지 크기**를 설정한다.
- **SaveState**: 기본적으로 **상태 저장**을 지원하며, 배치 잡이 중단되었을 때 마지막으로 읽은 위치를 저장하여 재시작 시 이어서 처리할 수 있도록 한다.
- **Exception Handling**: 읽기 과정에서 발생하는 예외를 처리하기 위해 다양한 리스너(**SkipListener**, **ReadListener**)를 지원한다.

### JdbcBatchItemWriter 개요

- Spring Batch에서 제공하는 `ItemWriter` 인터페이스를 구현하는 클래스이다.
- **JDBC를 통한 대량 데이터 저장**: JDBC를 통해 데이터를 저장하며, 대량 데이터 저장에 최적화되어 있다.
- **SQL 쿼리 정의**: SQL 쿼리를 **직접 작성**하여 원하는 방식으로 데이터베이스에 저장할 수 있다.
- **대용량 데이터 처리에 적합**: 데이터를 저장할 때 Chunk 기반으로 데이터를 처리하며, 커밋 간격에 따라 데이터베이스에 저장하여 안정적이고 효율적으로 데이터를 처리 힌다.


### JdbcBatchItemWriter 주요 구성 요소

- **DataSource**: 데이터베이스 **연결 정보**를 설정한다.
- **SqlStatementCreator**: INSERT, UPDATE, 또는 MERGE와 같은 **쿼리를 생성**하는 역할을 한다.
- **PreparedStatementSetter**: INSERT 쿼리의 **파라미터 값을 설정**하는 역할을 한다.
- **ItemSqlParameterSourceProvider**: Item 객체의 필드를 PreparedStatementSetter에 전달할 **쿼리 파라미터 값을 생성**하는 역할을 한다.

## 2. JdbcPagingItemReader 구현
