# 스프링 데이터 JPA

## 공통 인터페이스 설정

![Untitled](https://user-images.githubusercontent.com/106054507/190900052-7dbf8ba1-7c61-4f13-9a66-408edb5b6775.png)


- `org.springframework.data.repository.Repository` 를 구현한 클래스는 스캔 대상
    - `memberRepository.getClass() class com.sun.proxy.$ProxyXXX`
- `@Repository` 애노테이션 생략 가능
    - 컴포넌트 스캔을 스프링 데이터 JPA가 자동으로 처리
    - `JPA` 예외를 스프링 예외로 변환하는 과정도 자동으로 처리

![Untitled 1](https://user-images.githubusercontent.com/106054507/190900057-50de4a60-92ad-4e15-b118-cc8ef96fd184.png)


- **주요 메서드**
    - **save(S)** : 새로운 엔티티는 저장하고 이미 있는 엔티티는 병합한다.
    - **delete(T)** : 엔티티 하나를 삭제한다. 내부에서 `EntityManager.remove()` 호출
    - **findById(ID)** : 엔티티 하나를 조회한다. 내부에서 `EntityManager.find()`호출
    - **getOne(ID)** : 엔티티를 프록시로 조회한다. 내부에서 `EntityManager.getReference()` 호출
    - **findAll(…)** : 모든 엔티티를 조회한다. 정렬( `Sort` )이나 페이징( `Pageable`) 조건을 파라미터로 제공할 수 있음.
    

## 쿼리 메소드 기능

- **쿼리 메소드 기능 3가지**
    - 메소드 이름으로 쿼리 생성
    - 메소드 이름으로 `JPA NamedQuery` 호출
    - `@Query` 어노테이션을 사용해서 리파지토리 인터페이스에 쿼리 직접 정의
    
- **스프링 데이터 JPA가 제공하는 쿼리 메소드 기능**
    - **조회**: `find…By` , `read…By` , `query…By` , `get…By`,
    - **COUNT**: `count…By` 반환타입 `long`
    - **EXISTS**: `exists…By` 반환타입 `boolean`
    - **삭제**: `delete…By`, `remove…By` 반환타입 `long`
    - **DISTINCT**: `findDistinct`, `findMemberDistinctBy`
    - **LIMIT**: `findFirst3`, `findFirst`, `findTop`, `findTop3`
    
- **JPA NamedQuery**
    - `@NamedQuery` 어노테이션으로 `Named 쿼리` 정의
    - `@Query` 를 생략하고 메서드 이름만으로 `Named 쿼리`를 호출할 수 있음.

```java
@NamedQuery(
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = :username"
)
public class Member { }

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);
}
```

- **@Query, 리포지토리 메소드에 쿼리 정의하기**
    - 실행할 메서드에 정적 쿼리를 직접 작성하므로 이름 없는 `Named` 쿼리라 할 수 있음
    - `JPA Named` 쿼리처럼 애플리케이션 실행 시점에 문법 오류를 발견할 수 있음
    - `DTO`를 사용하여 조회하는 기능도 제공
    - 컬렉션 파라미터 바인딩 지원

```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

		@Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

		@Query("select m from Member m where m.username in :names")
		List<Member> findByNames(@Param("names") List<String> names)
}
```

- **반환 타입**
    - `스프링 데이터 JPA`는 유연한 반환 타입 지원
    - **컬렉션**
        - **결과 없음**: 빈 컬렉션 반환
    - **단건 조회**
        - **결과 없음**: `null` 반환
        - **결과가 2건 이상**: `javax.persistence.NonUniqueResultException` 예외 발생

```java
List<Member> findListByUsername(String username);
Member findMemberByUsername(String username);
Optional<Member> findOptionalByUsername(String username);
```

- **JPA 페이징, 정렬**
    - **페이징과 정렬 파라미터**
        - `org.springframework.data.domain.Sort` : 정렬 기능
        - `org.springframework.data.domain.Pageable` : 페이징 기능 (내부에 Sort 포함)
    - **특별한 반환 타입**
        - `org.springframework.data.domain.Page` : 추가 `count`쿼리 결과를 포함하는 페이징
        - `org.springframework.data.domain.Slice` : 추가 `count` 쿼리 없이 다음 페이지만 확인
        - `List (자바 컬렉션)`: 추가 `count` 쿼리 없이 결과만 반환

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
    Page<Member> findByAge(int age, Pageable pageable);
		Slice<Member> findByAge(int age, Pageable pageable);
}
```

- **벌크성 수정 쿼리**
    - 벌크성 쿼리 실행 후 영속성 컨텍스트 초기화: `@Modifying(clearAutomatically = true)`

```java
@Modifying(clearAutomatically = true)
@Query("update Member m set m.age = m.age + 1 where m.age >= :age")
int bulkAgePlus(@Param("age") int age);
```

- **@EntityGraph**
    - 연관된 엔티티들을 `SQL` 한번에 조회하는 방법
    - 스프링 데이터 JPA는 `JPA`가 제공하는 엔티티 그래프 기능을 편리하게 사용하게 도와준다.

```java
@Override
@EntityGraph(attributePaths = {"team"})
List<Member> findAll();
    
@EntityGraph(attributePaths = {"team"})
@Query
List<Member> findMemberEntityGraph();
    
@EntityGraph(attributePaths = {"team"})
List<Member> findEntityGraphByUsername(@Param("username") String username);
```

- **JPA Hint & Lock**
    - JPA 쿼리 힌트 (SQL 힌트가 아니라 JPA 구현체에게 제공하는 힌트)

```java
@QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
Member findReadOnlyByUsername(String username);

@Lock(LockModeType.PESSIMISTIC_WRITE)
List<Member> findLockByUsername(String username);
```

## 확장 기능

- **사용자 정의 리포지토리 구현**
    - **규칙**: 리포지토리 인터페이스 이름 + `Impl`
    - **스프링 데이터 JPA**가 인식해서 스프링 빈으로 등록

```java
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
		 private final EntityManager em;
		 
		 @Override
		 public List<Member> findMemberCustom() {
				 return em.createQuery("select m from Member m")
						 .getResultList();
		 }
}
```

- **Auditing**
    - 공통 관심사에 대해 자동화를 지원한다.

```java
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime lostModifiedDate;
    
    @CreatedBy
    @Column(updatable = false)
    private String createBy;
    
    @LastModifiedBy
    private String lostModifiedBy;
}
```

- **Web 확장 - 도메인 클래스 컨버터**
    - 도메인 클래스 컨버터가 중간에 동작해서 회원 엔티티 객체를 반환
    - 도메인 클래스 컨버터도 리파지토리를 사용해서 엔티티를 찾음

```java
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }
}
```

- **Web 확장 - 페이징과 정렬**
    - 파라미터로 `Pageable` 을 받을 수 있다.
    - 페이징 정보가 둘 이상이면 **접두사로 구분**
        - `@Qualifier 에 접두사명 추가 "{접두사명}_xxx”`
    - **Page 내용을 DTO로 변환**

```java
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/members")
    public Page<Member> list(Pageable pageable){
        return memberRepository.findAll(pageable);
    }

    @PostConstruct
    public void init(){
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("userA" + i, i));
        }
    }

		@GetMapping("/members1")
    public Page<Member> list1(@PageableDefault(size = 5) Pageable pageable){
        return memberRepository.findAll(pageable);
    }

		@GetMapping("/membersDto")
    public Page<MemberDto> listDto(Pageable pageable){
        Page<Member> page = memberRepository.findAll(pageable);
        Page<MemberDto> memberDto = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));
        return memberDto;
    }
}
```

```java
spring.data.web.pageable.default-page-size=20 /# 기본 페이지 사이즈/
spring.data.web.pageable.max-page-size=2000 /# 최대 페이지 사이즈/
```

## 스프링 데이터 JPA 분석

- 스프링 데이터 JPA가 제공하는 공통 인터페이스의 구현
- `org.springframework.data.jpa.repository.support.SimpleJpaRepository`
- `@Repository 적용`: `JPA 예외`를 스프링이 추상화한 예외로 변환
- `**@Transactional` 트랜잭션 적용**
    - JPA의 모든 변경은 트랜잭션 안에서 동작
    - 스프링 데이터 JPA는 변경(등록, 수정, 삭제) 메서드를 트랜잭션 처리
    - 서비스 계층에서 트랜잭션을 시작하지 않으면 리파지토리에서 트랜잭션 시작
    - 서비스 계층에서 트랜잭션을 시작하면 리파지토리는 해당 트랜잭션을 전파 받아서 사용
    - 그래서 스프링 데이터 JPA를 사용할 때 트랜잭션이 없어도 데이터 등록, 변경이 가능

- **새로운 엔티티를 구별하는 방법**

```java
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item implements Persistable<String> {

    @Id
    private String id;

    @CreatedDate
    private LocalDateTime createDate;

    public Item(String id) {
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return createDate == null;
    }

    @Override
    public String getId() {
        return id;
    }

}
```

## 나머지 기능

### **Projections**

- 엔티티 대신 `DTO`를 편리하게 조회할 때 사용
- **인터페이스 기반 Open Proejctions**

```java
public interface UsernameOnly {
    String getUsername();
}

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member> {
    List<UsernameOnly> findProjectionsByUsername(@Param("username") String username);
}
```

- **클래스 기반 Projections**

```java
public class UsernameOnlyDto {

    private final String username;

    public UsernameOnlyDto(String username) {
        this.username = username;
    }

    public String getUsername(){
        return username;
    }
}

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member> {
    List<UsernameOnlyDto> findClassProjectionsByUsername(@Param("username") String username);
}
```

- **동적 Projections**

```java
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member> {
		<T> List<T> findProjectionsByUsername(String username, Class<T> type);
}
List<UsernameOnly> result = memberRepository.findProjectionsByUsername("m1",UsernameOnly.class);
```

- **중첩구조 처리**

```java
public interface NestedClosedProjections {

    String getUsername();
    TeamInfo getTeam();

    interface TeamInfo{
        String getName();
    }
}
```

### 네이티브 쿼리

- **페이징 지원**
- **반환 타입**
    - `Object[]`,` Tuple` , `DTO`
- **제약**
    - `Sort` 파라미터를 통한 정렬이 정상 동작하지 않을 수 있음
    - `JPQL`처럼 애플리케이션 로딩 시점에 문법 확인 불가
    - 동적 쿼리 불가

```java
@Query(value = "select * from member where username = ?", nativeQuery = true)
Member findByNativeQuery(String username);
```

- **Projections 활용**
    - 페이징 지원

```java
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member> {
		@Query(value = "select m.member_id as id, m.username, t.name as teamName" +
            " from member m left join team t",
            countQuery = "select count(*) from member",
            nativeQuery = true)
    Page<MemberProjection>findByNativeProjection(Pageable pageable);
}

public interface NestedClosedProjections {

    String getUsername();
    TeamInfo getTeam();

    interface TeamInfo{
        String getName();
    }
}
```
