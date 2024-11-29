# Spring Batch Study 8

참고: [DEVOCEAN KIDO님 SpringBatch 연재 8](https://devocean.sk.com/blog/techBoardDetail.do?ID=166950)

---

Spring Batch의 `CompositeItemProcessor` 으로 여러 단계에 걸쳐 데이터 Transform 해보자.

## 1. CompositeItemProcessor

### 개요

- **CompositeItemProcessor**는 Spring Batch에서 제공하는 `ItemProcessor` 인터페이스를 구현하는 클래스이다.
- 여러 개의 ItemProcessor를 **하나의 Processor**로 연결하여, 데이터를 여러 단계의 처리를 수행할 수 있도록 한다.

### 주요 구성 요소

- **Delegates**: 처리에 사용할 ItemProcessor들의 목록이다. CompositeItemProcessor는 이 리스트에 포함된 각 Processor를 순차적으로 호출한다.
- **TransactionAttribute**: 트랜잭션 속성을 설정하여 각 단계의 데이터 처리가 하나의 트랜잭션에서 수행될지 여부를 결정한다.

### CompositeItemProcessor 동작 원리

- **초기 입력**: ItemReader에서 데이터를 읽어 첫 번째 ItemProcessor로 전한다.
- **단계별 처리**: 각 ItemProcessor는 데이터를 변환하거나 가공하며, 변환된 데이터를 다음 ItemProcessor로 넘긴다.
- **최종 출력**: 마지막 ItemProcessor에서 처리된 결과는 ItemWriter로 전달된다.

### 장점

- **단계별 처리**
  - 여러 단계로 나누어 처리를 수행하여 코드를 명확하고 이해하기 쉽게 만들 수 있다.
  - 단계별로 테스트 및 디버깅이 용이하다.
- **재사용 가능성**
  - 각 Processor는 독립적으로 설계되므로, 다른 Job이나 Step에서도 재사용할 수 있다.
  - 비즈니스 로직의 모듈화(Modularity)가 가능해진다.
- **유연성**
  - 다양한 ItemProcessor를 조합하여 원하는 처리 과정을 구현할 수 있다.

### 단점
- **설정 복잡성**
  - 여러 개의 Processor를 설정하고 관리해야 하기 때문에 설정이 복잡해질 수 있다.
  - Processor의 순서를 잘못 설정하거나 의존 관계를 잘못 설계하면 의도한 결과를 얻지 못할 수 있다.
- **성능 저하**
  - 각 Processor를 순차적으로 호출하기 때문에, 단계 수가 많아질수록 성능에 영향을 줄 수 있다.

<br>

---

## 2. CompositeItemProcessor 구현

