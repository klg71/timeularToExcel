plugins {
    kotlin("jvm") version "1.6.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenCentral()
}
dependencies {
    // Kotlin
    implementation(kotlin("reflect"))


    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

    implementation("io.github.openfeign:feign-core:11.8")
    implementation("io.github.openfeign:feign-jackson:11.8")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.2")
}

tasks{
    withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>(){
        manifest {
            attributes["Main-Class"] = "net.mayope.timeularToExcel.MainKt"
        }
    }
}
