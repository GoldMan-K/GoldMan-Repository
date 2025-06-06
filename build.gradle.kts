plugins {
    id("org.springframework.boot") version "3.2.6"
    id("io.spring.dependency-management") version "1.1.5"
    id("java")
}

// Node.js와 npm 태스크 설정
// Windows 환경 여부 확인
val isWindows = System.getProperty("os.name").toLowerCase().contains("win")
val npmCommand = if (isWindows) "npm.cmd" else "npm"

task<Exec>("installNpmDependencies") {
    workingDir("src/frontend")
    commandLine(npmCommand, "install")
}

task<Exec>("buildFrontend") {
    dependsOn("installNpmDependencies")
    workingDir("src/frontend")
    commandLine(npmCommand, "run", "build")
}

tasks.processResources {
    dependsOn("buildFrontend")
}


group = "communityboard"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // JUnit BOM 및 JUnit Jupiter
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // Lombok 의존성
    implementation("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    // Spring Data JPA 의존성 추가
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web") // 웹 관련 설정(필요 시)
    //mysql-connector
    runtimeOnly("com.mysql:mysql-connector-j")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    mainClass.set("com.community.backend.home.HomeController")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21)) // Java 21 명시적으로 설정
    }
}


tasks.test {
    useJUnitPlatform()
}

