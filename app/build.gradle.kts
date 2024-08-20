plugins {
        id("com.android.application")
        kotlin("android")
        id("com.google.devtools.ksp")
        id("kotlin-parcelize")
        id("dagger.hilt.android.plugin")
        id("org.jetbrains.kotlin.plugin.serialization")
        alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.natife.timeforce"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.alerion.timeforce"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += "-Xcontext-receivers"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {

    implementation(libs.androidx.browser)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.ui)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.material.icons.extended)
    //Serialization
    implementation(libs.kotlinx.serialization.json)
    // Navigation Components
    implementation(libs.androidx.navigation.compose)
    //DataStore
    implementation(libs.androidx.datastore.preferences)
    //DaggerHilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.hilt.navigation.compose)

    ksp(libs.hilt.compiler)
    //Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //noinspection GradleDependency
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}