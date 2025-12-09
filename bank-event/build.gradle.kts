dependencies {
    implementation(project(":bank-domain"))
    implementation(project(":bank-core"))
    implementation(project(":bank-monitoring"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Log
    implementation("ch.qos.logback:logback-classic:1.5.21")

    // Retry
    implementation("org.springframework.retry:spring-retry")
}
