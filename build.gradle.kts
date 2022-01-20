buildscript {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    kotlin("jvm") version "1.6.10"
    //id("myproject.kotlin-conventions")
    id("org.hibernate.build.xjc") version "2.2.0"
}

group = "net.codesup.util"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("org.glassfish.jaxb:jaxb-core:3.0.2")
    implementation("org.glassfish.jaxb:jaxb-xjc:3.0.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "13"
    }
}

