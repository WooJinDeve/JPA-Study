# QueryDsl

## QueryDsl 설정

```java
buildscript {
 ext {
		 queryDslVersion = "5.0.0"
	 }
}

plugins {
	 id 'org.springframework.boot' version '2.6.2'
	 id 'io.spring.dependency-management' version '1.0.11.RELEASE'

	 //querydsl 추가
	 id "com.ewerk.gradle.plugins.querydsl" version "1.0.10"
	 id 'java'
}

group = 'com.example'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '11'

configurations {
		 compileOnly {
			 extendsFrom annotationProcessor
	 }
}

repositories {
	 mavenCentral()
}

dependencies {
		 implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
		 implementation 'org.springframework.boot:spring-boot-starter-web'
		 
		 //querydsl 추가
		 implementation "com.querydsl:querydsl-jpa:${queryDslVersion}"
		 annotationProcessor "com.querydsl:querydsl-apt:${queryDslVersion}"
		 compileOnly 'org.projectlombok:lombok'
		 runtimeOnly 'com.h2database:h2'
		 annotationProcessor 'org.projectlombok:lombok'

		 //테스트에서 lombok 사용
		 testCompileOnly 'org.projectlombok:lombok'
		 testAnnotationProcessor 'org.projectlombok:lombok'
}

test {
	 useJUnitPlatform()
}

//querydsl 추가 시작
def querydslDir = "$buildDir/generated/querydsl"

querydsl {
	 jpa = true
	 querydslSourcesDir = querydslDir
}

sourceSets {
	 main.java.srcDir querydslDir
}

configurations {
	 querydsl.extendsFrom compileClasspath
}

compileQuerydsl {
	 options.annotationProcessorPath = configurations.querydsl
}
//querydsl 추가 끝
```

- `Gradle → compileJava` : Qfile 생성
- `Gradle → clean` : Qfile 삭제

## 기본문법

- **기본 Q-Type 활용**

```java
QMember qMember = new QMember("m"); //별칭 직접 지정
QMember qMember = QMember.member; //기본 인스턴스 사용
```

- **검색 조건 쿼리**
    - 검색 조건은 `.and()` , `. or()` 를 메서드 체인으로 연결할 수 있음.
    - `where()` 에 파라미터로 검색조건을 추가하면 `AND` 조건이 추가됨
    - `null` 값은 무시 메서드 추출을 활용해서 동적 쿼리를 깔끔하게 만들 수 있음

```java
member.username.eq("member1") // username = 'member1'
member.username.ne("member1") //username != 'member1'
member.username.eq("member1").not() // username != 'member1'
member.username.isNotNull() //이름이 is not null
member.age.in(10, 20) // age in (10,20)
member.age.notIn(10, 20) // age not in (10, 20)
member.age.between(10,30) //between 10, 30
member.age.goe(30) // age >= 30
member.age.gt(30) // age > 30
member.age.loe(30) // age <= 30
member.age.lt(30) // age < 30
member.username.like("member%") //like 검색
member.username.contains("member") // like ‘%member%’ 검색
member.username.startsWith("member") //like ‘member%’ 검색
```

- **결과 조회**
    - `fetch()` : 리스트 조회, 데이터 없으면 빈 리스트 반환
    - `fetchOne()` : 단 건 조회
        - 결과가 없으면 : `null`
        - 결과가 둘 이상이면 :  `com.querydsl.core.NonUniqueResultException`
    - `fetchFirst()` : `limit(1).fetchOne()`
    - `fetchResults()` : 페이징 정보 포함, `total count` 쿼리 추가 실행
    - `fetchCount()` : `count` 쿼리로 변경해서 `count` 수 조회
    - `fetchResults(), fetchCount()` : **QueryDsl 5.0부터 Deprecated 예정**

- **정렬**
    - 회원 나이 내림차순(`desc`)
    - 회원 이름 올림차순(`asc`)
    - 회원의 이름이 없으면 마지막에 출력(`nulls last`)

```java
List<Member> result = query
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();
```

- **페이징**

```java
List<Member> result = query.selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();
```

- **집합**
    - `JPQL`이 제공하는 모든 집합 함수를 제공
    - `groupBy` , 그룹화된 결과를 제한하려면 `having`

```java
List<Tuple> result = query.select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                )
                .from(member)
                .fetch();

// 팀의 이름과 각 팀의 평균 연령을 구해라
List<Tuple> result = query.select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();
```

- **기본 조인**
    - 조인의 기본 문법은 첫 번째 파라미터에 조인 대상을 지정하고, 두 번째 파라미터에 별칭(`alias`)으로 사용할 `Q 타입`을 지정하면 된다
    - `join()` , `innerJoin()` : 내부 조인( `inner join`)
    - `leftJoin()` : `left` 외부 조인( `left outer join` )
    - `rightJoin()` : `rigth` 외부 조인( `rigth outer join` )
- **세타 조인**
    - 연관관계가 없는 필드로 조인
    - `from` 절에 여러 엔티티를 선택해서 세타 조인

```java
// 기본 조인
// 팀 A에 소속된 모든 회원
List<Member> result = query.selectFrom(member)
            .join(member.team, team)
            .where(team.name.eq("teamA"))
            .fetch();

assertThat(result)
            .extracting("username")
            .containsExactly("member1", "member2");

// 세타 조인(연관관계가 없는 필드로 조인)
// 회원의 이름이 팀 이름과 같은 회원 조회
List<Member> result = query.select(member)
             .from(member, team)
             .where(member.username.eq(team.name))
             .fetch();
```

- **조인 - on절**
    1. **조인 대상 필터링**
        - 외부조인이 아니라 내부조인( `inner join`)을 사용하면, `where 절`에서 필터링 하는 것과 기능이 동일
    2. **연관관계 없는 엔티티 외부 조인**
        - `leftJoin()` 부분에 일반 조인과 다르게 엔티티 하나만 들어감
            - **일반조인**: `leftJoin(member.team, team)`
            - **on조인**: `from(member).leftJoin(team).on(xxx)`

```java
// 예 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
// JPQL : select m, t from Member m left join m.team t on t.name = 'teamA'
List<Tuple> result = query.select(member, team)
                .from(member)
                .leftJoin(member.team, team).on(team.name.eq("teamA"))
                .fetch();

// 연관관계가 없는 엔티티 외부 조인
// 회원의 이름이 팀 이름과 같은 대상 외부 조인
List<Tuple> result = query
                .select(member, team)
                .from(member)
                .leftJoin(team).on(member.username.eq(team.name))
                .fetch();
```

- **조인 - 페치 조인**
    - 페치 조인은 `SQL`에서 제공하는 기능은 아님
    - `SQL조인`을 활용해서 연관된 엔티티를 `SQL` 한번에 조회하는 기능

```java
Member findMember = query.select(member)
                .from(member)
                .join(member.team, team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();
```

- **서브 쿼리**
    - `com.query.jpa.JPAExpressions` 사용
    - `from` 절의 서브쿼리(인라인 뷰)는 지원하지 않는다
    - **from 절의 서브쿼리 해결방안**
        1. 서브쿼리를 `join`으로 변경
        2. 애플리케이션에서 쿼리를 2번 분리해서 실행
        3. `nativeSQL`을 사용

```java
QMember memberSub = new QMember("memberSub");

// 나이가 가장 많은 회원 조회
List<Member> result = query
					  .selectFrom(member)
            .where(member.age.eq(
                    JPAExpressions
                            .select(memberSub.age.max())
                            .from(memberSub)
            )).fetch();

// 나이가 평균 이상인 회원 조회
List<Member> result = query
						 .selectFrom(member)
             .where(member.age.goe(
                     JPAExpressions
                            .select(memberSub.age.avg())
                            .from(memberSub)
               ).fetch();

// In 사용
List<Member> result = query.selectFrom(member)
             .where(member.age.in(
                     JPAExpressions
                            .select(memberSub.age)
                             .from(memberSub)
                             .where(memberSub.age.gt(10))
              )).fetch();
// Select Sub Query
List<Tuple> result = query
                .select(member.username,
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub))
                .from(member)
                .fetch();
```

- **Case 문**
    - `select`, 조건절에서 사용가능
    - 복잡한 조건을 변수로 선언해서 `select`, `orderBy` 절에서 함께 사용할 수 있다

```java
List<String> result = query.select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타"))
                .from(member)
                .fetch();

List<String> result = query.select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타"))
                .from(member)
                .fetch();
```

- **상수, 문자 더하기**

```java
List<Tuple> result = query
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetch();

List<String> result = query
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .where(member.username.eq("member1"))
                .fetch();
```

## 중급 문법

- **프로젝션과 결과 반환 - 기본**
    - **프로젝션** : `select` 대상 지정
    - 프로젝션 대상이 하나면 타입을 명확하게 지정할 수 있음
    - 프로젝션 대상이 둘 이상이면 튜플이나 `DTO`로 조회
- **Querydsl 빈 생성(Bean population)**
    - 프로퍼티 접근
    - 필드 직접 접근
    - 생성자 사용

```java
// 프로퍼티 접근 : 기본 생성자 필요
List<MemberDto> result = query
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

// 필드 직접 접근
List<MemberDto> result = query
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

// 생성자 사용
List<MemberDto> result = query
                .select(Projections.constructor(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

// 서브 쿼리 사용
QMember memberSub = new QMember("memberSub");
List<UserDto> result = query.select(Projections.fields(UserDto.class,
                    member.username.as("name"),
                    ExpressionUtils.as(select(memberSub.age.max())
                            .from(memberSub), "age")
            ))
            .from(member)
            .fetch();
```

- **프로젝션과 결과 반환 - @QueryProjection**
    - 컴파일러로 타입을 체크할 수 있으므로 가장 안전한 방법
    - 이전의 방법에 비해 `QueryDsl`에 의존적인 방법
    - `DTO`에 `QueryDSL` 어노테이션을 유지해야 하는 점과 `DTO`까지 Q 파일을 생성해야 함.

```java
@Data
@NoArgsConstructor
public class MemberDto {

    private String username;
    private int age;

    @QueryProjection
    public MemberDto(String username, int age) {
        this.username = username;
        this.age = age;
    }
}

@Test
public void findDtoByQueryProjection(){
    List<MemberDto> result = query.select(new QMemberDto(member.username, member.age))
            .from(member)
            .fetch();
}
```

- **동적 쿼리 - BooleanBuilder 사용**

```java
//**BooleanBuilder**
private List<Member> searchMember1(String usernameCond, Integer ageCond) {
    BooleanBuilder builder = new BooleanBuilder();

    if (StringUtils.hasText(usernameCond)) {
        builder.and(member.username.eq(usernameCond));
    }

    if (ageCond != null) {
        builder.and(member.age.eq(ageCond));
    }

    return query.selectFrom(member)
            .where(builder)
            .fetch();
}
```

- **동적 쿼리 - Where 다중 파라미터 사용**
    - `where` 조건에 `null` 값은 무시된다.
    - 메서드를 다른 쿼리에서도 재활용 할 수 있다. ( 조합 가능 )

```java
private List<Member> searchMember2(String usernameCond, Integer ageCond) {
    return query
            .selectFrom(member)
            .where(usernameEq(usernameCond), ageEq(ageCond))
            .fetch();
}

private BooleanExpression usernameEq(String usernameCond) {
    return StringUtils.hasText(usernameCond) ? member.username.eq(usernameCond) : null;
}

private BooleanExpression ageEq(Integer ageCond) {
    return ageCond != null ? member.age.eq(ageCond) : null;
}

private BooleanExpression allEq(String usernameCond, Integer ageCond){
      return usernameEq(usernameCond).and(ageEq(ageCond));
}
```

- **수정, 삭제 배치 쿼리**
    - `JPQL` 배치와 마찬가지로, 영속성 컨텍스트에 있는 엔티티를 무시하고 실행되기 때문에 배치 쿼리를 실행하고 나면 영속성 컨텍스트를 초기화 하는 것이 안전

```java
long count = query
             .update(member)
             .set(member.username, "비회원")
             .where(member.age.lt(28))
             .execute();

long count = query
             .update(member)
						 .set(member.age, member.age.add(1))
             .execute();
```

- **SQL function 호출하기**
    - `SQL function`은 `JPA`와 같이 `Dialect`에 등록된 내용만 호출할 수 있음

```java
List<String> result = query
                .select(Expressions.stringTemplate(
                        "function('replace', {0}, {1}, {2})",
                        member.username, "member", "M"))
                .from(member)
                .fetch();

List<String> result = query
                .select(member.username)
                .from(member)
                //.where(member.username.eq(
                // Expressions.stringTemplate("function('lower', {0})", member.username)))
                .where(member.username.eq(member.username.lower()))
								.fetch();
```

ㅇ