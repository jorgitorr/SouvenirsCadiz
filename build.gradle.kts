// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
    id("org.jetbrains.dokka") version "1.9.20"
    //DCS - Dagger Hilt
    id("com.google.dagger.hilt.android") version "2.44" apply false
    //detekt analizador de codigo
    id("io.gitlab.arturbosch.detekt") version "1.23.0"
}