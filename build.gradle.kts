plugins {
    kotlin("jvm") version "1.4.30"
    application
}

group = "com.github.neblung.puzzle"
version = "0.1-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

application {
    mainClass.set("com.github.neblung.puzzle.MainKt")
}

// https://docs.gradle.org/current/userguide/working_with_files.html#sec:creating_uber_jar_example
tasks.register<Jar>("uberJar") {
    archiveFileName.set("uber.jar")

    manifest {
        attributes["Main-Class"] = application.mainClass
    }

    from(sourceSets.main.get().output)
    from(configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) })
}

dependencies {
    val kotestVersion = "4.3.1"
    val junit5Version = "5.7.0"

    implementation(kotlin("stdlib"))

    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit5Version")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit5Version")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
