plugins {
    id("application")
    id("org.springframework.boot") version "3.2.6"
    id("io.spring.dependency-management") version "1.1.5"
    id("java")
    // 프런트 빌드용(Node/npm 자동 다운로드; Jenkins에 Node 설치 불필요)
    id("com.github.node-gradle.node") version "7.0.2"
}

/* ───────── 공통 설정 ───────── */
group = "communityboard"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    mainClass.set("com.community.CommunityApplication")
}

springBoot {
    mainClass.set("com.community.CommunityApplication")
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // DB
    runtimeOnly("com.mysql:mysql-connector-j")

    // Lombok (권장 스코프)
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

    // Test (Junit5)
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test { useJUnitPlatform() }

/* ───────── Frontend (Vue CLI) ─────────
 * 프로젝트 안에 Node/npm을 내려받아 사용(재현성 ↑)
 */
node {
    download.set(true)
    version.set("20.14.0")
    npmVersion.set("10.8.2")
    // 프런트엔드 package.json 위치
    nodeProjectDir.set(file("src/frontend"))
}

/** npm ci (package-lock.json이 있으면 권장; 없으면 "install"로 바꿔도 됨) */
val npmCi = tasks.register("npmCi", com.github.gradle.node.npm.task.NpmTask::class) {
    workingDir.set(file("src/frontend"))
    args.set(listOf("ci"))
}

/** npm run build (package.json의 scripts.build 실행) */
val npmBuild = tasks.register("npmBuild", com.github.gradle.node.npm.task.NpmTask::class) {
    dependsOn(npmCi)
    workingDir.set(file("src/frontend"))
    args.set(listOf("run", "build"))
}

/** 프런트 산출물(dist)을 정적 리소스로 포함 → JAR 내부 /static/** */
tasks.processResources {
dependsOn(npmBuild)
from("src/frontend/dist") { into("static") }
}
