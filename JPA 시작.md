# JPA 시작

## SQL 중심적인 개발의 문제점

- 무한 반복, 지루한 코드
    - `CRUD`
    - `객체 → SQL`, `SQL → 객체`
- SQL에 의존적인 개발을 피하기 어렵다.
- 패러타임의 불일치
    - 객체 VS 관계형 데이터베이스

## JPA

- `Java Persistence API`
- 자바 진영의 `ORM` 기술 표준이다.
- **ORM**
    - `Object-relational mappping`(객체 관계 매핑)
    - 객체는 객체대로 설계
    - 관계형 데이터베이스는 관계형 데이터베이스 대로 설계
    - `ORM` 프레임워크가 중간에서 매핑
- `JPA`는 애플리케이션과 `JDBC` 사이에서 동작
    - 개발자 요청 → JPA → JDBC → DB → JDBC → JPA → 개발자
    - JPA : `Entity` 분석, `SQL` 생성, `JDBC API` 사용, 패러다임 불일치 해결
    - JDBC : `SQL → DB`, `DB → result`

## JPA의 성능 최적화 기능

- **1차 캐시와 동일성 보장**
    - 같은 트랙잭선 안에서는 같은 엔티티를 반환한다.
    - `DB Isolation Level`이 `Read Commit`이어도 애플리케이션에서 `Repeatable Read` 보장한다.
- **트랜잭션을 지원하는 쓰기 지연**
    - **INSERT**
        - 트랜잭션을 커밋할 때까지 `INSERT SQL`을 모음
        - `JDBC BATCH SQL` 기능을 사용해서 한번에 `SQL` 전송
    - **UPDATE**
        - `UPDATE`, `DELETE`로 인한 로우락 시간 최소화
        - 트랜잭션 커밋 시 `UPDATE`, `DELETE` SQL 실행하고,  바로 커밋
- **지연 로딩**
    - 지연 로딩 : 객체가 실제 사용될 때 로딩한다.
    - 즉시로딩 : `JOIN SQL`로 한번에 연관된 객체까지 미리 조회한다.

## JPA의 데이터베이스 방언

- JPA는 특정 데이터베이스에 종속적이지 않는다.
- 각각의 데이터베이스가 제공하는 SQL 문법과 함수는 조금씩 다르다.
    - 가변 문자: `MySQL : VARCHAR`, `Oracle : VARCHAR2`
    - 문자열을 분리 함수: `SQL 표준 : SUBSTRING()`, `Oracle : SUBSTR()`
    - 페이징: `MySQL : LIMIT` , `Oracle : ROWNUM`
- 방언
    - SQL 표준을 지키지 않는 특정 데이터베이스만의 고유한 기능이다.

## JPA 구동 방식


- Persistence 에서, `yml`, `application.properties`, `persisitence.xml` 를 통해 설정 정보 조회한다.
- `EntityManagerFactory`를 통한 생성한다.
- `EntityManagetFactory`에서 요청에 의해 `EntityManager` 생성한다.

```java
public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        //Transaction
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setId(2L);
            member.setName("helloB");
            em.persist(member);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }finally {
            em.close();
        }

        emf.close();
    }
}
```

- 엔티티 매니저 팩토리는 하나만 생성해서 애플리케이션 전체에서 공유해야 한다.
- 엔티티 매니저는 쓰레드간에 공유하면 안되며, 사용하고 버려야 한다.
- JPA의 모든 데이터 변경은 트랜잭션 안에서 실행해야 한다.

## JPQL

- JPA는 SQL을 추상화한 JPQL이라는 객체 지향 쿼리 언어 제공한다.
- 테이블이 아닌 객체를 대상으로 검색하는 객체 지향 쿼리이다.
- SQL을 추상화해서 특정 데이터베이스 SQL에 의존적이지 않는다.
