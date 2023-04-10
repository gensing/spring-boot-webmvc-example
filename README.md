# spring-boot-example
spring 기술을 사용하여, 서비스 개발에 필요한 기본 코드 샘플을 작성한 프로젝트입니다.

***

## 요구사항
- Java 17
- Docker, Docker-Compose
***

## 프로젝트 실행 방법

- build 시 가상 컨테이너를 띄워 테스트하는 작업이 있습니다. docker-compose 작업과 동시에 수행 될시 컴터 사양에 따라 타임 아웃이 발생할 수 있습니다. 아래의 스크립트 순서로 각 스텝 완료 후 진행 바랍니다.

```
$ git clone https://github.com/gensing/spring-boot-example.git
$ cd spring-boot-example
$ gradlew clean build
$ cd docker
$ docker-compose -f docker-compose.yml up -d
$ cd ./../
$ java -jar ./build/libs/boot-0.0.1-SNAPSHOT.jar --spring.profiles.active=local 
```

***

## 라이브러리
- Spring Boot Starter Web
- Spring Boot Starter Security
- Spring Boot Starter Cache
- Spring Boot Starter Data JPA
- Spring Boot Starter Data Redis
- Spring Boot Starter Data Elasticsearch
- Spring REST Docs
- ePages Rest Docs Api Spec
- Querydsl
- MapStruct
- Lombok

***

## 개발
- Member Service
- Post Service
- Security with jwt
- Exception handling
- Immutable Properties
- Test
