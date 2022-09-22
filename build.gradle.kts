import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
}

group = "ml.rektsky"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.ow2.asm:asm:9.3")
    implementation("org.ow2.asm:asm-util:9.3")
    implementation("org.ow2.asm:asm-commons:9.3")
    implementation("com.github.fan87:Regular-Bytecode-Expression:2.1.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}