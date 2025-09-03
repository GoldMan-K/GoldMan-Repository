# 1단계: 빌드
FROM openjdk:21-jdk-slim as builder
WORKDIR /app

# Gradle 빌드 결과 JAR 복사
ARG JAR_FILE=build/libs/communityboard-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# 2단계: 런타임
FROM openjdk:21-jdk-slim

WORKDIR /app

# 필수 패키지 설치
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# 애플리케이션 사용자 생성
RUN groupadd -r appuser && useradd -r -g appuser appuser

# JAR 파일 복사
COPY --from=builder /app/app.jar app.jar

# 권한 변경
RUN chown -R appuser:appuser /app
USER appuser

# 포트 및 헬스체크
EXPOSE 7777
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:7777/actuator/health || exit 1

# JVM 옵션
ENV JAVA_OPTS="-Xmx512m -Xms256m -Dspring.profiles.active=prod"

# 애플리케이션 실행
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
