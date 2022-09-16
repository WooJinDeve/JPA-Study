# 개요

- [김영한의 스프링 부트와 JPA 실무 완전 정복 로드맵](https://www.inflearn.com/roadmaps/149)를 통해 배운것들을 정리한 Repository입니다.
-  강의 프로젝트 파일에는 강의를 위한 **기본코드만 제공**됩니다.

# JPA 기본 강의 세팅
- **JPA BASIC SETTING**
    - `Java Version` : 8
    - `Project` : `Maven Project`
    - `pom.xml`
        - `org.hibernate.hibernate-entitymanager.5.4.13.Final`, `com.h2database.h2.1.4.199`

# 스프링 시작

- **스프링 시작**
    - [https://start.spring.io/](https://start.spring.io/)


- **Spring Package Setting**
    - `Java Version` : 11
    - `Project` : `Gradle Project`
    - `Packaging`
        - `Spring Boot` : `Jar`
    - `Dependencies`
        - `Spring Web`, `Thymeleaf`, `Lombok`, `H2 DATABASE`, `Spring DATA JPA`
        - `Query Param Log` : `implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.8.0'`


- **IDE Setting**
    - **Lombok Setting**
        - `setting` -> `Bulid, Execution, Depolyment` -> `Annotation Processors` -> `Enable annotation processing ✔`
    - **Encoding Setting**
        - `setting` -> `File Encodings` -> `Global Encoding` -> `UTF-8`
        - `setting` -> `File Encodings` -> `Properties Files` -> `Default encoding for properties files` -> `UTF-8`

- **H2 DataBase**
    - **Download**
        - [H2 DataBase Download URL](https://www.h2database.com)
    - **Setting**
        - `JDBC URL` : `jdbc:h2:tcp://localhost/~/test`
        - `User` : `sa`

# 공부

<details>
<summary><h3>JPA 기본</h3></summary>

- [SQL 중심적인 개발의 문제점](https://github.com/WooJinDeve/JPA-Study/issues/1#issue-1368870103)
- JPA?
- JPA의 성능 최적화 기능
- JPA의 데이터베이스 방언
- JPA 구동 방식
- JPQL

</details>

<details>
<summary><h3>영속성 관리 - 내부 동작 방식</h3></summary>

- [엔티티 매니저 팩토리와 엔티티 매니저](https://github.com/WooJinDeve/JPA-Study/issues/2#issue-1368886016)
- 영속성 컨텍스트
- 영속성 컨텍스트의 이점
- 플러시
- 준영속 상태

</details>

<details>
<summary><h3>엔티티 매핑</h3></summary>

- [엔티티 매핑 소개](https://github.com/WooJinDeve/JPA-Study/issues/3#issue-1368918263)
- 데이터 베이스 스키마 자동 생성
- 기본 키 매핑

</details>

<details>
<summary><h3>연관관계 매핑</h3></summary>

- [기초 용어](https://github.com/WooJinDeve/JPA-Study/issues/4#issue-1369374160)
- 데이터 중심의 설계의 문제점
- 단방향 연관관계
- 양방향 연관관계
- 연관관계의 주인(Owner)

</details>

<details>
<summary><h3>다양한 연관관계 매핑</h3></summary>

- [연관관계 매핑시 고려사항](https://github.com/WooJinDeve/JPA-Study/issues/5#issue-1369501872)
- 다대일 [N:1]
- 일대다 [1:N]
- 일대일 [1:1]
- 다대다 [N:M]

</details>

<details>
<summary><h3>고급 매핑</h3></summary>

- [상속관계 매핑](https://github.com/WooJinDeve/JPA-Study/issues/6#issue-1369650022)
- 주요 어노테이션
- 조인 전략
- 단일 테이블 전략
- 구현 클래스마다 테이블 전략
- @MappedSuperclass

</details>

<details>
<summary><h3>프록시와 연관관계 관리</h3></summary>

- [프록시](https://github.com/WooJinDeve/JPA-Study/issues/7#issue-1369788892)
- 즉시로딩, 지연로딩
- 영속성 전이 : CASCADE
- 고아객체
- 영속성 전이 + 고아 객체 생명주기

</details>

<details>
<summary><h3>값 타입</h3></summary>

- [기본 값 타입](https://github.com/WooJinDeve/JPA-Study/issues/8#issue-1370085853)
- 임베디드 타입
- 값 타입과 불변 객체
- 값 타입 비교
- 값 타입 컬렉션

</details>

<details>
<summary><h3>객체지향 쿼리 언어 - 기본 문법</h3></summary>

- [JPQL](https://github.com/WooJinDeve/JPA-Study/issues/9#issue-1371226456)
- JPA Criteria
- QueryDSL
- 네이티브 SQL
- JDBC 직접 사용
- JPQL 문법
- 프로젝션
- 페이징
- 조인
- 서브쿼리
- JPQL 타입 표현
- 조건식 - CASE 식
- JPQL 함수
- 경로 표현식
- 패치조인(fetch join)
- 페치조인의 한계
- 다형성 쿼리
- 엔티티 직접 사용
- Named 쿼리
- 벌크 연산

</details>

<details>
<summary><h3>OSIV와 성능 최적화</h3></summary>

- [OSIV?](https://github.com/WooJinDeve/JPA-Study/issues/10#issue-1375753568)
- OSIV ON
- OSIV OFF
- 커멘드와 쿼리 분리

</details>
