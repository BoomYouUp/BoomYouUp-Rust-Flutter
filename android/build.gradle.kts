//val kotlinVersion by extra { "1.8.0" }

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    /*dependencies {
        classpath 'com.android.tools.build:gradle:7.3.1'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    }*/
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    kotlin("android") apply false
    id("com.android.application") apply false
    //id("com.android.library") apply false
}

rootProject.buildDir = file("../build")

subprojects {
    project.buildDir = file("${rootProject.buildDir}/${project.name}")
}
subprojects {
    project.evaluationDependsOn(":app")
}

tasks.register("clean") {
    delete(rootProject.buildDir)
}
