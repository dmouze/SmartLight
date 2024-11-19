buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.gradle) // Update to the latest version
        classpath(libs.kotlin.gradle.plugin) // Update to the latest version
        classpath(libs.google.services) // Add this line
    }
}

plugins {
    id("com.android.application") version "8.7.2" apply false
    id("com.android.library") version "8.7.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}