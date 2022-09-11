# 개요

- [김영한의 스프링 부트와 JPA 실무 완전 정복 로드맵](https://www.inflearn.com/roadmaps/149)를 통해 배운것들을 정리한 Repository입니다.

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