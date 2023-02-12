## 1. 기술 스택
- openJDK : 1.8
- Spring boot : 2.6.2
- build : gradle
- database : mariaDB
- Spring data JPA
- Querydsl : 5.0.0
- lombok

## 2. 빌드 방법
gradle을 통해 빌드하는 점, 로컬 빌드로 진행하는 점, 8081 포트를 사용하는 것입니다. clone 받을 branch는 master branch입니다.

### 1. DB를 직접 생성
1. feeboard > src > main > resources 하위에 `shcema.sql`을 보시면 freeboard에 사용하는 DB table의 DDL이 들어 있습니다.
2. `schema.sql`에 있는 DDL로 각 테이블을 생성한 후 동일한 위치에 있는 `application.yml`을 확인합니다.
3. DB connection 설정을 변경하기 위해 Spring: > datasource: 의 하위의 url을 연결하고자 하는 DB의 url 호스트로 변경합니다. username과 password도 마찬가지로 연결하고자 하는 DB에서 설정된 username과 password로 변경합니다.
4. Spring: > jpa: > hibernate: 의 하위에 주석 처리된 `ddl-auto: create`를 주석 해제 한 후 IDE를 통해 `FreeboardApplication.java`를 실행해 프로젝트를 빌드합니다.
5. 빌드 완료 후 연관 관계 매핑으로 사용할 테이블인 board_recommend_list와 user_recommend_list가 정상적으로 생성되었는지 확인합니다.
6. 실행된 application을 종료 후 빌드 할 때마다 테이블이 새로 생성되는 것을 방지하기 위해 `ddl-auto: create`을 다시 주석 처리 합니다.

## 3. API 스펙 정리

### 1. 고려 사항
- URI의 경우 동작을 나타내는 단어가 포함된 컨트롤 URI를 최대한 지양하고 대신 method를 통해 중점적으로 분리했습니다.
- 조회는 get, 생성은 post, 수정은 patch, 삭제 및 취소는 delete로 분리 했습니다. 다만 좋아요 처리 method의 경우 좋아요 이력이 없으면 recommend 테이블에 신규 row를 추가하고, 지금은 취소 상태지만 이전 좋아요 이력을 통해 row가 존재하면 해당 row의 recommend value를 true로 바꿔주는 동작을 같이 수행하기 때문에 데이터를 새로 엎어 쓰는 put보다 post가 적합하다고 판단했습니다.
- accoutType이 들어가는 URI는 조금 아쉬움이 남습니다. URI와 패키지명은 최대한 단일 단어의 구조를 유지해야 추후의 확장성이 고려되는데 이 부분에 대해 미처 고려하지 못하고 캐멀 케이스를 포함한 URI 구조를 만들었습니다.

상세 스펙 정리 문서 : https://docs.google.com/spreadsheets/d/1wmCh0tccI5f_rIHvzPJAYdyR2jNPF9vzIhfptkth6Wo/edit#gid=0

## 4. 기술 선정

### 1. Maven과 gradle

빌드 툴에 대한 선정 고민은 다음과 같았습니다. 현재 콜버스랩에서 사용하고 있는 maven으로 선택할지, 토이 프로젝트에서 주로 사용하는 gradle로 사용할지에 대한 고민, 즉 가고 싶은 회사의 스펙에 맞출지 나에게 익숙한 빌드 툴을 사용할지였습니다.

그래서 내린 답은 Maven이었습니다. 재직 중인 회사에서도 maven을 선호 하기도 했고, 도입해보기로 결정한 JPA와 Querydsl의 경우 maven으로 처음 해보기 때문에 좋은 경험이 될거라고 생각했습니다.

그러나 문제는 생각보다 컸습니다. maven의 경우, 테스트 코드로 동작 여부를 확인한 후 본 프로젝트를 빌드하면 잘 동작하다가 어느 순간 hikariCP에 문제가 생겼습니다.

아직 해당 오류에 대해 명확한 해결 포인트를 찾지 못해 gradle로 넘어왔습니다.

발생한 해당 오류는 이후에도 계속 찾아봐서 해결책을 블로그에 기재해볼 예정입니다.

### 2. JPA와 Mybatis

Mybatis의 경우 기술적 난이도가 JPA에 비해 낮습니다. JPA처럼 오류가 발생했을 때 어느 부분, DB에 쏴진 쿼리의 어떤 코드가 오류를 일으켰는지 찾기가 훨씬 쉽습니다. JDBC로 처리하는 상당 부분의 코드와 파라미터 설정 및 결과 매핑을 간편하게 해주기 때문에 무엇을 select 해올지 전혀 모르겠다면 Map으로 쏴서 무엇이든 받아올 수 있습니다. 또한 자바 코드가 SQL 쿼리문과 분리 되기 때문에 생각보다 코드가 간결해지는 결과를 볼 수 있습니다.

그러나 이것은 Mybatis를 원하는 방향으로 긍정적으로 사용했을 때의 강점입니다. 일반적으로 기술 난이도는 낮지만 개발 과정에서 개발자가 SQL 코드부터 결과를 객체와 매핑해주는 것 까지 직접 다 처리해야 합니다. 결과 매핑을 간편하게 해준다는 강점은 테이블 구조가 바뀔 때 마다 해당 테이블을 바라보고 있는 VO와 DTO가 모두 바뀌어야 한다는 것입니다. 바뀐 객체를 수정하면서 연관된 모든 SQL 쿼리도 바꿔줘야 하게 되는데 이 경우 쿼리를 분리하고자 하던 Mybatis의 의도와는 다르게 백엔드가 SQL 의존적으로 개발되게 됩니다.

자바는 엄연히 객체의 세상에서 개발하는 언어로 객체가 지향하는 목표와 관계형 DB가 가지는 목표는 엄연히 다릅니다.

객체는 참조를 가지고 연관된 객체를 찾아가지만 관계형 DB는 참조가 아닌 PK와 FK를 통해 연관된 테이블을 찾습니다. 객체의 단방향 참조가 SQL 쿼리의 양방향 참조와 충돌하게 되면서 자바를 자바답게 개발하기 어렵게 됩니다.
가장 심각한 문제는 객체 안에 특정 쿼리가 날아가서 무슨 값과 필드를 가져왔는지 해당 객체를 직접 보지 않는 이상 예측을 할 수 없게 됩니다. 즉, 엔티티가 가지고 있는 값에 대해 신뢰 문제가 커지게 됩니다.

그러한 이유로 백엔드의 객체에 대한 설계와 DB의 테이블 설계를 분리하고 백엔드가 SQL에 의존적으로 개발하는 현상을 피하면서 엔티티에 대한 신뢰도 문제, 코드의 생산성을 고려해 JPA를 선택하게 되었습니다.

### 3. Querydsl

개발자 관점에서 보면 JPA를 쓸 경우 조인 등 복잡한 SQL 쿼리를 수행할 때는 결국 JPQL이나 Native SQL을 통해 SQL을 직접 작성해 DB에 쏴주게 됩니다.

그러나 이러한 경우 잘못된 SQL 쿼리를 작성하게 될 경우 잘못 작성된 쿼리는 컴파일 시점이 아니라 실행되어 호출되는 런타임 시점에 오류를 일으킵니다. 잘못 작성된 코드를 조기에 찾을 수 없게 됩니다.

또한 SQL 의존적 개발을 피하기 위해 JPA를 선택했는데 다시 또 SQL을 작성하게 된다면 문제를 해결하는 게 아니라 동일한 문제를 다른 형태로 겪는 것과 같다고 생각했습니다.

Native SQL이나 JPQL처럼 쿼리를 문자열로 작성하는 경우 조건식등 메소드로 빼내서 공통화 시킬 수 있는 부분을 처리하기 어렵습니다.

그러나 querydsl을 사용하게 되면 잘못 작성된 querydsl은 객체 참조와 동일하게 동작하기 때문에 컴파일 시점에 이를 잡아낼 수 있습니다. 또한  자바의 객체와 메소드를 참조 하듯이 작성할 수 있습니다. 자바 백엔드를 개발하는데 SQL 구문 등 다른 언어를 신경쓸 필요 없이 백엔드 언어인 자바에만 집중해서 개발할 수 있습니다. 이러한 이유로 querydsl을 선택했습니다.

- querydsl이 적용된 코드는 각 도메인의 repository 패키지 하위의 custom 패키지에서 확인할 수 있습니다.

### 4. AWS RDS

프로젝트를 빌드할 때 결국 JPA등 JDBC와 관련된 라이브러리를 사용할 경우 DB connection은 굉장히 중요해집니다. 유효하지 않는 DB로 connection을 시도할 경우 빌드 단계에서부터 실패합니다.

DDL을 포함하고 있는 schema.sql이 있지만 결국 해당 SQL 방언은 mySQL에 맞춰져 있습니다. 아예 다른 방언을 사용하는 DB의 경우 DDL의 정상 동작 조차 보장할 수 없습니다.

이러한 이유로 DB를 제가 구성한 AWS RDS에 연결해 어떠한 환경에도 빌드할 수 있도록 RDS로 DB 배포를 진행해보았습니다.

> AWS RDS는 개인 정보 문제로 해당 repository에 정보를 올리지 않았습니다.

## 5. 패키지 구조

```bash
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── sample
    │   │           └── freeboard
    │   │               ├── FreeboardApplication.java
    │   │               ├── domain
    │   │               │   ├── account
    │   │               │   │   ├── controller
    │   │               │   │   │   └── AccountTypeController.java
    │   │               │   │   ├── domain
    │   │               │   │   │   ├── AccountType.java  
    │   │               │   │   │   └── TypeEnum.java
    │   │               │   │   ├── dto
    │   │               │   │   │   ├── AccountTypeDTO.java  
    │   │               │   │   │   └── AccountTypes.java
    │   │               │   │   ├── repository
    │   │               │   │   │   └── AccountTypeRepository.java
    │   │               │   │   └── service
    │   │               │   │       ├── AccountTypeService.java 
    │   │               │   │       └── AuthService.java
    │   │               │   ├── board
    │   │               │   │   ├── controller
    │   │               │   │   │   └── BoardController.java
    │   │               │   │   ├── domain
    │   │               │   │   │   └── Board.java
    │   │               │   │   ├── dto
    │   │               │   │   │   ├── BoardDTO.java
    │   │               │   │   │   ├── BoardOwnResponse.java
    │   │               │   │   │   ├── BoardResponse.java
    │   │               │   │   │   └── Boards.java
    │   │               │   │   ├── repository
    │   │               │   │   │   ├── custom
    │   │               │   │   │   │   ├── BoardRepositoryCustom.java
    │   │               │   │   │   │   └── BoardRepositoryImpl.java
    │   │               │   │   │   └── BoardRepository.java
    │   │               │   │   └── service
    │   │               │   │       └── BoardService.java
    │   │               │   ├── recommend
    │   │               │   │   ├── domain
    │   │               │   │   │   └── recommend.java
    │   │               │   │   ├── dto
    │   │               │   │   │   ├── RecommendResponse.java
    │   │               │   │   │   └── Recommends.java
    │   │               │   │   ├── repository
    │   │               │   │   │   │   ├── RecommendRepositoryCustom.java
    │   │               │   │   │   │   └── RecommendRepositoryImpl.java
    │   │               │   │   │   └── RecommendRepository.java
    │   │               │   │   └── service
    │   │               │   │       └── RecommendService.java
    │   │               │   └── user
    │   │               │       ├── controller
    │   │               │       │   └── UserController.java
    │   │               │       ├── domain
    │   │               │       │   └── User.java
    │   │               │       ├── dto
    │   │               │       │   ├── UserDTO.java
    │   │               │       │   └── UserRequest.java
    │   │               │       ├── repository
    │   │               │       └── service
    │   │               │           └── UserService.java
    │   │               └── global
    │   │                   ├── annotation
    │   │                   │   └── AnyoneCallable.java
    │   │                   ├── aspect
    │   │                   │   ├── Auth.java
    │   │                   │   └── PointCut.java
    │   │                   ├── collection
    │   │                   │   └── FirstClassCollection.java
    │   │                   ├── constant
    │   │                   │   └── Message.java
    │   │                   ├── dto
    │   │                   │   ├── filter
    │   │                   │   │   └── Pagenation.java
    │   │                   │   └── response
    │   │                   │       └── ListResponse.java
    │   │                   └── errorhandler
    │   │                       └── exception
    │   │                           ├── BadRequestException.java
    │   │                           ├── ForbiddenException.java
    │   │                           ├── NotFoundException.java
    │   │                           └── UnauthorizedException.java
    │   │             
    │   └── resources
    │       ├── static
    │       ├── templates
    │       ├── application.yml
    │       └── schema.yml
``` 

패키지 구조는 제가 지향하는 설계 방법을 최대한 적용해보았습니다. 이러한 방법이 좋은 프로젝트를 생성하는 방법은 아니겠지만 코드를 통해 전달되기 어려운 몇 가지 사항을 글로 조금이나마 전달드리고자 합니다.

## 1. DTO

JPA를 학습하면서 가장 중요하게 다뤄야 하는 것 중 하나가 바로 entity와 DTO라고 생각합니다. JPA의 연관 관계에 걸린 문제를 DTO가 아주 적절히 해결해주고 있다고 생각합니다.

먼저 DTO를 사용하지 않고 entity 자체를 바로 리턴해줄 경우 겪는 문제는 순환 참조 입니다. JPA는 기본적으로 객체 참조에 대한 형태를 가지고 있습니다. 양방향으로 매핑된 객체의 경우 서로를 바라보고 있기 때문에 직렬화를 하게 되면 닭이 먼저냐 달걀이 먼저냐 처럼 서로가 무한히 참조하게 됩니다. 이 때문에 toString 등 서로가 동일하게 참조할만한 메소드등은 참조하지 않는 변수로만 작성합니다.

이러한 직렬화 과정은 주로 엔티티를 response로 내어줄 때 jackson이 json으로 파싱하는 과정에서 발생합니다. 많은 블로그 글들이 `@JsonManagedReference` 어노테이션을 이용해 강제적으로 json 파싱을 방지하고자 하지만 이는 결코 이 문제의 근본적인 해결책이 아닐뿐더러 설계의 결함을 가지고 있습니다.

entity는 JDBC가 DB에 던져줄 때, 혹은 결과값을 받아올 때 사용하는 DB의 구조를 그대로 가지고 있는 객체입니다. 즉, DB 계층에서 사용하는 객체입니다. 이러한 객체가 외부, 즉 백엔드를 빠져나와 다른 계층(프론트엔드)으로 노출이 되면 해당 계층은 DB 계층에 의존적으로 바뀝니다. DB와는 전혀 상관 없는 로직을 수행하는 프론트엔드 계층이 DB 계층에 대해 의존적으로 바뀌는 결과를 가집니다. 또한 최종 사용자 관점에서 보면 화면 뒷단에서 사용하는 DB의 구조를 훤히 알게 되는 일이 발생합니다. 어떠한 형태로든 직간접적으로 비즈니스 로직의 구현을 노출하는 셈이 됩니다.

프론트엔드의 관점에서 보면 entity로 최종적으로 응답하는 response의 경우 불변성이 보장되지 않습니다. 비즈니스 로직을 그대로 수행하면서 set된 객체입니다. 정상적인 비즈니스 로직을 수행했다고 하더라도 중간에 오브젝트를 탈취해 setter를 통해 값을 바꿔 버릴 수 있다는 것입니다. 백엔드가 엔티티의 신뢰 문제로 JPA를 도입했는데 도입한 JPA로 인해 역으로 프론트엔드가 response에 대한 신뢰 문제를 겪게 됩니다.

DTO를 쓰면 순환 참조, DB 계층 노출 및 의존 생성, response의 불변 보장이라는 문제를 한 번에 해결할 수 있습니다. DTO는 오직 getter와 생성자만 가지고 있고, 생성하는 시점에 연관 관계 참조가 일어날 부분을 분해해서 생성하기 때문에 순환 참조가 발생하지 않습니다. DB 구조를 그대로 가지고 있지 않기 때문에 백엔드 외부의 계층이 DB의 변경에 전혀 영향을 받지 않게 됩니다. setter가 없기 때문에 비즈니스 로직을 다 수행하고 return 해주는 상황 이후에는 값이 탈취당해 바뀔 염려가 없습니다.

위의 이유로 DTO를 중심으로 사용하고, 최대한 setter를 사용하지 않은 이유입니다.

DTO에 대한 정리글 : https://velog.io/@power0080/JPA%EC%8F%98%EC%95%84%EC%98%AC%EB%A6%B0-JPA%EC%9D%98-%EC%88%9C%ED%99%98-%EC%B0%B8%EC%A1%B0-JsonManagedReference%EC%97%90%EC%84%9C-%EB%B3%BC%EA%B9%8C-DTO%EB%A1%9C-%EB%B3%BC%EA%B9%8C 

### 2. Service와 Repository의 1대1 대응

작성한 코드를 보시면 UserService에서 BoardRepository를 호출해 Board의 값을 바꾸거나 조회하지 않습니다. UserService에서 Board에 대한 값이 필요하면 BoardService를 호출해 받아옵니다. Board와 관련된 로직, DB 접근은 오로지 BoardService의 책임으로 분리를 함으로써 DB의 변경에 대해 변경이 발생한 지점을 명확하게 파악하고 하나의 로직에 대해 단일 책임을 더욱 더 강화했습니다.

### 3. AnyoneCallable

`@AnyoneCallable` 어노테이션은 Spring AOP 동작을 수행합니다. 커뮤니티의 경우 사용자가 확인되어야 수행 가능한 기능과 누구라도 접근할 수 있는 기능 두 개로 나눠서 볼 수 있습니다. request header의 Authentication의 값으로 사용자 인증을 확인합니다.

Spring AOP를 통해 컨트롤러의 수행 전에 사전에 인증 작업을 할 수 있는 기능을 구현했습니다. 물론 Spring security 처럼 토큰을 통한 정교한 인증이 아닌 Authentication에 있는 accountId 값으로 회원 여부를 확인하는 단순한 수준의 구현이었습니다.

해당 어노테이션이 붙어 있으면 누구든지 접근할 수 있는 서비스이지만 어노테이션이 없다면 사용자가 확인되어야 하는 서비스로 분리함으로써 일관된 동작을 보장하기도 하지만, 코드를 보는 사람이 서비스의 동작 전 필요한 것은 무엇인지 예측하기 쉽도록 작성했습니다.

다만 한 가지 아쉬운 부분은 시간이 조금 더 있었다면 AOP를 통한 심플한 인증에 더해 Spirng Security를 적용해 정교한 controller 분기를 해보고 싶었고, 한 번 인증된 사용자의 경우 redis에 인증 정보를 올려 놓고 매번 DB에서 인증 정보를 찾아와야 하는 상황을 적용 해보았으면 하는 아쉬움이 남습니다.

### 4. 일급 컬렉션

최근 자바지기 박재성님의 자바_플레이그라운드_with_TDD_클린코드를 수강하면서 일급 컬렉션에 흥미가 생겼습니다.

DTO와 마찬가지로 불변의 속성을 가지면서 해당 컬렉션의 validate를 쉽게 할 수 있을 것이라고 생각해 적용해보았습니다.

ListResponse는 이러한 일급 컬렉션을 리턴해주는 역할을 수행합니다. 물론 해당 컬렉션이 무엇을 의미하는지 의미의 직관성은 많이 올라갔지만 비즈니스 로직을 검증할 수 있는 요소에 대해서는 아쉽게 적용하지 못했습니다.

### 5. 상속을 지양해야 할까

상속은 상위 클래스의 코드에 대해 추측을 해야 하고 캡슐화를 헤치는 경우가 많습니다. 상위 클래스의 내부 로직을 알고 있어야 하위 클래스를 제대로 사용할 수 있는 경우가 자주 발생합니다. override라는 방법이 있지만 이미 수행되고 있는 로직을 재정의할 경우 오류를 일으킬 가능성이 큽니다.

그래서 최대한 불필요한 상속을 지양하려고 했지만 이 경우 중복된 코드의 처리와 맞물리게 됩니다. 중복된 코드를 줄여 최대한 가독성과 재사용성 측면을 조금 더 강화 하고자 했습니다.

물론 현재 객체 지향이 추구하는 방향은 재사용성 보다는 유연성이 더 중요한 개념이 되어 앞으로 수행하게 될 프로젝트에서 찾아야할 절충안에 대해 고민해볼 수 있는 좋은 시간이었습니다.

### JPA의 조회 방식
각 서비스들에서 데이터를 조회해오는 경우 (예시 : userService.findById) find와 get을 JPA에서 사용하는 용법에 맞춰 분리를 했습니다.

혼용 처럼 보일 수 있지만 JPA의 조회 방식을 보면 find는 값이 없다면 null을, get은 값이 없다면 exception을 발생시킵니다.

이러한 이유로 각각의 서비스에서 데이터를 조회 하는 함수의 경우, find와 get을 사용해 get을 사용했을 때는 데이터가 올바르지 않거나 유효성 검증을 통과하지 못했을 경우 exception을 발생 시키도록 처리했습니다.


