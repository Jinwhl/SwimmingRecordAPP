// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}

buildscript {
    repositories {
        // maven { url("https://maven.google.com") } for Gradle <= 3
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
        google()
    }
    dependencies {
        classpath("com.google.android.gms:oss-licenses-plugin:0.10.6")
    }
}

//allprojects {
//    repositories {
//        maven {"https://oss.sonatype.org/content/repositories/snapshots/"}
//    }
//}