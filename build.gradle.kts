buildscript {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    kotlin("jvm") version "2.2.20"
}

group = "net.codesup.util"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(24)
    sourceSets {
        val test by getting {
            kotlin.srcDirs("src/test/kotlin", "${layout.buildDirectory}/generated-sources/test")
        }
    }
}



dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
}

