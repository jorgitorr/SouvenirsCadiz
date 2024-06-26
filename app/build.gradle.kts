plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("org.jetbrains.dokka")
    //DCS - Dagger Hilt
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
}



android {
    namespace = "com.example.souvenirscadiz"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.souvenirscadiz"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {
    //Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.44")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    //splash
    implementation("androidx.core:core-splashscreen:1.0.1")
    //coil
    implementation("io.coil-kt:coil-compose:2.6.0")
    //storage
    implementation("com.google.firebase:firebase-storage-ktx")
    //google maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.maps.android:maps-compose:1.0.0")
    //dokka
    implementation("org.jetbrains.dokka:android-documentation-plugin:1.9.20")
    //CSV
    implementation("com.opencsv:opencsv:4.6")
    //NavHost
    implementation("androidx.navigation:navigation-compose:2.7.7")
    // DCS - Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-analytics")

    //authentication con google
    implementation("com.google.android.gms:play-services-auth:21.0.0")

    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    // https://firebase.google.com/docs/android/setup#available-libraries
    // DCS - Servicio de Autenticación
    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
    //authenticacion de Firebase por google
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    // DCS - Base de datos Firestore
    implementation("com.google.firebase:firebase-firestore-ktx:24.11.1")
    //iconos
    implementation ("androidx.compose.material:material-icons-core:1.6.5")
    implementation ("androidx.compose.material:material-icons-extended:1.6.5")
    //dependencias antiguas
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

// DCS - Dagger Hilt - Allow references to generated code
kapt {
    correctErrorTypes = true
}