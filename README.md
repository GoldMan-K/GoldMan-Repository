# 커뮤니티 게시판

## 프로젝트 소개

이 프로젝트는 Spring Boot 백엔드와 Vue.js 프론트엔드를 사용한 커뮤니티 게시판 웹 애플리케이션입니다.

## 기술 스택

### 백엔드
- Java 21
- Spring Boot
- Spring Data JPA
- MySQL

### 프론트엔드
- Vue.js 3.2.0
- Axios

## 시작하기

### 백엔드 실행

```bash
./gradlew bootRun
```

백엔드 서버는 기본적으로 `http://localhost:7777`에서 실행됩니다.

### 프론트엔드 개발 서버 실행

```bash
cd src/frontend
npm install
npm run serve
```

프론트엔드 개발 서버는 기본적으로 `http://localhost:8080`에서 실행됩니다.

## 빌드 및 배포

프로젝트 전체 빌드:

```bash
./gradlew build
```

이 명령은 프론트엔드 빌드 결과물을 Spring Boot의 정적 리소스 디렉토리에 복사하여 단일 JAR 파일로 패키징합니다.
