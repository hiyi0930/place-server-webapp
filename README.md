# 장소 검색 서비스 (Server)
## 목차
- [개발 환경](#개발-환경)
- [빌드 및 실행](#빌드-및-실행)
- [테스트 계정](#테스트-계정)
- [사용된 오픈소스 및 용도](#사용된-오픈소스-및-용도)

## 개발 환경
- 기본 환경
    - Server
        - Kotlin (target version 11)
        - Spring Boot
        - JPA
        - H2
        - Gradle
        - Junit5
    
## 빌드 및 실행
```
$ git clone https://github.com/hiyi0930/place-server-webapp.git
$ cd place-server-webapp
$ ./gradlew clean build
$ java -jar -Dspring.profiles.active=dev build/libs/place-server-webapp-0.0.1-SNAPSHOT.jar

```

## 테스트 계정
- ID: test1 ~ test10 중 선택
- Password : ID와 동일

## 사용된 오픈소스 및 용도
- spring-boot-starter-cache(ehcache)
    - API 응답 결과 캐싱
- spring-rest-docs
    - API 가이드 자동 생성
- spring-security-crypto
    - Bcrypt 비밀번호 암호화
- jjwt
    - JWT 토큰 생성 및 검증
- springmockk && junit jupiter
    - Kotlin 기반 Mock 테스트
