dependencies {
    implementation(project(":bank-domain"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Log
    implementation("ch.qos.logback:logback-classic:1.5.21")

    // Retry
    implementation("org.springframework.retry:spring-retry")
}
