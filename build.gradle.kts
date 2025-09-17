buildscript {
    repositories {
        gradlePluginPortal()
    }
}
val kotlinVersion: String by properties

plugins {
    kotlin("jvm")
}

group = "net.codesup.util"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(24)
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
}

