plugins {
    kotlin("jvm")
    kotlin("plugin.allopen")
    id("io.quarkus")

    id("io.gatling.gradle") version "3.9.0.2"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
    id("com.github.ben-manes.versions") version "0.44.0"
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("$quarkusPlatformGroupId:$quarkusPlatformArtifactId:$quarkusPlatformVersion"))
    implementation("io.quarkus:quarkus-resteasy-reactive-jackson")
    implementation("io.quarkus:quarkus-resteasy-reactive")
    implementation("io.quarkus:quarkus-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.quarkus:quarkus-arc")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")

    gatlingImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.1")
}

group = "io.github.simonscholz"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
allOpen {
    annotation("javax.ws.rs.Path")
    annotation("javax.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    kotlinOptions.javaParameters = true
}

gatling {
    // WARNING: options below only work when logback config file isn't provided
    logLevel = "WARN" // logback root level
    logHttp = io.gatling.gradle.LogHttp.NONE // set to 'ALL' for all HTTP traffic in TRACE, 'FAILURES' for failed HTTP traffic in DEBUG
}
