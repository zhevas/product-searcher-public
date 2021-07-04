import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.google.cloud.tools.jib") version "2.8.0"
    id("org.springframework.boot") version "2.4.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.hidetake.swagger.generator") version "2.18.2" apply false
    kotlin("jvm") version "1.4.31"
    kotlin("plugin.spring") version "1.4.31"
}

group = "ru.kardel"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    jcenter()
}

buildscript {
    dependencies {
        classpath("io.spring.gradle:dependency-management-plugin:1.0.10.RELEASE")
    }
}

apply(plugin = "io.spring.dependency-management")

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2020.0.2")
        mavenBom("com.google.cloud:spring-cloud-gcp-dependencies:2.0.2")
        mavenBom("com.google.cloud:libraries-bom:19.2.1")
    }
}

dependencies {
    implementation("com.google.cloud:google-cloud-vision")
    implementation("com.google.cloud:spring-cloud-gcp-starter")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.googlecode.objectify:objectify:6.0.7")
    implementation("io.springfox:springfox-boot-starter:3.0.0")
    implementation("io.springfox:springfox-swagger-ui:3.0.0")
    implementation("org.apache.poi:poi-ooxml:3.15")
    implementation("io.github.microutils:kotlin-logging:1.7.8")
    implementation("org.postgresql:postgresql:42.2.16")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

apply("build-swagger.gradle")

jib {
    to {
        image = "eu.gcr.io/api-project-808331037133/core"
        tags = setOf("latest", project.version.toString())
    }
    container {
        mainClass = "ru.kardel.core.ApplicationKt"
        ports = listOf("8080")
    }
}
