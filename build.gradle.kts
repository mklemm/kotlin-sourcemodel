import org.jetbrains.kotlin.konan.file.File.Companion.userHome
import org.jreleaser.model.Active
import org.jreleaser.model.Signing.Mode

buildscript {
    repositories {
        gradlePluginPortal()
    }
}
val kotlinVersion: String by properties

plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
    id("org.jreleaser") version "1.20.0"
    id("org.jetbrains.dokka") version "2.0.0"
}

group = "net.codesup.util"
version = "0.2.0-alpha"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(24)
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        register<MavenPublication>("kotlin-sourcemodel") {
            from(components["java"])
            pom {
                name = "kotlin-sourcemodel"
                description = "Source Code Builder for the Kotlin programming language"
                url = "https://github.com/mklemm/kotlin-sourcemodel"
                inceptionYear = "2025"
                licenses {
                    license {
                        name = "MIT License"
                        url = "https://opensource.org/license/mit"
                    }
                }
                developers {
                    developer {
                        id.set("mklemm")
                        name = "Mirko Klemm"
                        email = "mklemm@codesup.net"
                    }
                }
                scm {
                    connection = "scm:git:https//github.com/mklemm/kotlin-sourcemodel.git"
                    developerConnection = "scm:git:https//github.com/mklemm/kotlin-sourcemodel.git"
                    url = "https://github.com/mklemm/kotlin-sourcemodel"
                }
            }
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/mklemm/kotlin-sourcemodel")
            credentials {
                username = project.findProperty("github.username") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("github.token") as String? ?: System.getenv("TOKEN")
            }
        }
        maven {
            url = layout.buildDirectory.dir("staging-deploy").get().asFile.toURI()
        }
    }
}

val gradleProject = project

jreleaser {
    project {
        description = "Source Code Generation for the Kotlin programming language"
        authors.add("Mirko Klemm")
        license.set("MIT")
        links {
            homepage = "https://github.com/mklemm/kotlin-sourcemodel"
        }
        inceptionYear = "2025"
    }
    release {
        github {
            repoOwner = gradleProject.findProperty("github.username") as String? ?: System.getenv("USERNAME")
            overwrite = true
            token = gradleProject.findProperty("github.token") as String? ?: System.getenv("TOKEN")
        }
    }
    signing {
        active = Active.ALWAYS
        armored = true
        mode = Mode.FILE
        publicKey = "${userHome.path}/.gnupg/public.pgp"
        secretKey = "${userHome.path}/.gnupg/private.pgp"
    }
    deploy {
        maven {
            active = Active.ALWAYS
            mavenCentral {
                create("sonatype") {
                    active = Active.ALWAYS
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository(layout.buildDirectory.dir("staging-deploy").get().toString())
                }
            }
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
}

