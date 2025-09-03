# 멀티스테이지 빌드를 위한 베이스 이미지
FROM openjdk:21-jdk-slim as builder

# 작업 디렉토리 설정
WORKDIR /app

# JAR 파일 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 실행 단계
FROM openjdk:21-jdk-slim

# 시스템 업데이트 및 필요한 패키지 설치
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# 애플리케이션 사용자 생성
RUN groupadd -r appuser && useradd -r -g appuser appuser

# 작업 디렉토리 설정
WORKDIR /app

# JAR 파일 복사
COPY --from=builder /app/app.jar app.jar

# 소유권 변경
RUN chown -R appuser:appuser /app

# 사용자 변경
USER appuser

# 포트 노출
EXPOSE 7777

# 헬스체크 추가
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:7777/actuator/health || exit 1

# JVM 옵션 설정
ENV JAVA_OPTS="-Xmx512m -Xms256m -Dspring.profiles.active=prod"

# 애플리케이션 실행
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

