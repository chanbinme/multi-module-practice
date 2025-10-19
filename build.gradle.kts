// apply false로 설정하면 하위 모듈에서 플러그인을 개별적으로 적용할 수 있습니다.
plugins {
    kotlin("jvm") version "1.9.25" apply false
    kotlin("plugin.spring") version "1.9.25" apply false
    kotlin("plugin.jpa") version "1.9.25" apply false
    id("org.springframework.boot") version "3.5.6" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
}

// 모든 하위 모듈에 공통 설정을 적용합니다.
allprojects {
    group = "com.chanbinme"
    version = "0.0.1-SNAPSHOT"
    description = "multi-module-practice"
    repositories {
        mavenCentral()
    }
}

// 하위 모듈별로 플러그인과 설정을 적용합니다.
subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "io.spring.dependency-management")

    if (name == "bank-api") {
        apply(plugin = "org.springframework.boot")
        apply(plugin = "kotlin-spring")
        apply(plugin = "kotlin-jpa")
    }

    if (name == "bank-core") {
        apply(plugin = "kotlin-spring")
    }

    if (name == "bank-domain") {
        apply(plugin = "kotlin-spring")
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "21"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}