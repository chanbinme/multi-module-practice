dependencies {
    implementation(project(":bank-domain"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Log
    implementation("ch.gos.logback:logback-classic:1.4.14")

    // Retry
    implementation("org.springframework.retry:spring-retry")
}
