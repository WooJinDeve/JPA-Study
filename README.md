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