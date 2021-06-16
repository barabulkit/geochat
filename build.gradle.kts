import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.10"
    kotlin("plugin.spring") version "1.5.10"
}

group = "xyz.barabulkit"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
    maven { setUrl("https://dl.bintray.com/kotlin/exposed") }
    maven { setUrl("https://52north.org/maven/repo/releases/")}
    maven { setUrl("https://dl.bintray.com/sdeleuze/maven/") }
    maven { setUrl("https://jitpack.io") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("junit:junit:4.12")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.jetbrains.exposed:exposed:0.8.5")
    implementation("org.jetbrains.exposed:spring-transaction:0.8.5")
    implementation("net.postgis:postgis-jdbc:2.2.1") {
        exclude(module = "postgresql")
    }
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.github.mayconbordin:postgis-geojson:1.0") {
        exclude(module = "postgresql")
    }
    implementation("org.postgresql:postgresql:42.2.21")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks {
    test {
        useJUnit()
    }
}