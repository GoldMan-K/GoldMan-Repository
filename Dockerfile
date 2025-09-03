# -------------------------------
# 1️.빌드 단계 (Builder)
# -------------------------------
FROM openjdk:21-jdk-slim AS builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 빌드 산출물 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# -------------------------------
# 2️.실행 단계 (Run)
# -------------------------------
FROM openjdk:21-jdk-slim

# 필요한 패키지 설치
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# 애플리케이션 사용자 생성
RUN groupadd -r appuser && useradd -r -g appuser appuser

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 단계에서 만든 JAR 복사
COPY --from=builder /app/app.jar app.jar

# 권한 부여 (읽기/실행 가능)
RUN chown -R appuser:appuser /app && chmod 644 /app/app.jar

# 사용자 변경
USER appuser

# 포트 노출
EXPOSE 7777

# 헬스체크
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:7777/actuator/health || exit 1

# JVM 옵션 설정
ENV JAVA_OPTS="-Xmx512m -Xms256m -Dspring.profiles.active=prod"

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
