plugins {
    kotlin("jvm")
    kotlin("plugin.allopen")
    id("io.quarkus")

    id("io.gatling.gradle") version "3.9.2.1"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
    id("com.github.ben-manes.versions") version "0.44.0"
    id("com.google.cloud.tools.jib") version "3.3.1"
}

repositories {
    mavenCentral()
    mavenLocal()
}

val gatlingJibDocker: Configuration by configurations.creating {
    extendsFrom(
        configurations.kotlinCompilerPluginClasspathGatling.get(),
        configurations.gatling.get(),
    )
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
    implementation("io.quarkus:quarkus-micrometer-registry-prometheus")

    // Messaging
    implementation("io.quarkus:quarkus-smallrye-reactive-messaging")

    // JMS
    implementation("io.quarkus:quarkus-smallrye-reactive-messaging-amqp")

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

tasks.register("gatlingJar", Jar::class) {
    group = "build"
    archiveBaseName.set("gatling-performance-analysis")
    dependsOn("gatlingClasses", "processResources")

    manifest {
        attributes["Implementation-Title"] = "Gradle Jar File Example"
        attributes["Implementation-Version"] = archiveVersion
        attributes["Main-Class"] = "io.gatling.app.Gatling"
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(sourceSets.gatling.get().output)
    from(configurations.kotlinCompilerPluginClasspathGatling.get().map { if (it.isDirectory) it else zipTree(it) })
    from(configurations.gatling.get().map { if (it.isDirectory) it else zipTree(it) }) {
        exclude("META-INF/MANIFEST.MF")
        exclude("META-INF/*.SF")
        exclude("META-INF/*.DSA")
        exclude("META-INF/*.RSA")
    }
    with(tasks.jar.get() as CopySpec)
}

// Copy over the gatling classes to the app/classes folder
tasks.register("appDir", Copy::class) {
    dependsOn("gatlingClasses")
    from("build/classes/kotlin/gatling")
    into("build/extra-directory/app/classes/")
}

tasks.named("jib") {
    dependsOn("appDir")
}

tasks.named("jibDockerBuild") {
    dependsOn("appDir")
}

jib {
    configurationName.set("gatlingJibDocker")
    extraDirectories.setPaths("build/extra-directory")
    container {
        mainClass = "io.gatling.app.Gatling"
        args = listOf("-s", "io.github.simonscholz.simulation.HelloSimulation")
    }
}
