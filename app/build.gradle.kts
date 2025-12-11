plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.jetbrainsKotlinSerialization)
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.parcelize)
    alias(libs.plugins.kotlinCompose)
    alias(libs.plugins.secrets)
}


android {
    namespace = "com.example.tierraburritoapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tierraburritoapp"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    flavorDimensions += "env"
    productFlavors {
        create("development") {
            dimension = "env"
            buildConfigField("String", "URL_TIERRA_BURRITO", "\"http://192.168.3.120:8080\"")
            buildConfigField("String", "URL_API_GOOGLE", "\"https://maps.googleapis.com/\"")
            buildConfigField(
                "String",
                "URL_API_OPENROUTE_SERVICE",
                "\"https://api.openrouteservice.org/\""
            )

            testInstrumentationRunner = "dagger.hilt.android.testing.HiltAndroidJUnitRunner"
        }
        create("production") {
            dimension = "env"
            buildConfigField("String", "URL_TIERRA_BURRITO", "\"http://192.168.3.120:8080\"")
            buildConfigField("String", "URL_API_GOOGLE", "\"https://maps.googleapis.com/\"")
            buildConfigField(
                "String",
                "URL_API_OPENROUTE_SERVICE",
                "\"https://api.openrouteservice.org/\""
            )
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.6.10"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/LICENSE*"
            excludes += "META-INF/NOTICE*"
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/NOTICE.txt"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/NOTICE.md"
        }
    }

}




dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.timber)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(platform(libs.androidx.compose.bom))

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.converter.scalars)
    implementation(libs.logging.interceptor)

    implementation(libs.coil)
    implementation(libs.coil.compose)

    // Google Maps
    implementation(libs.google.maps)
    implementation(libs.maps.compose)


    // Hilt
    implementation(libs.hilt.core)
    implementation(libs.hilt.compose)
    implementation(libs.androidx.core.splashscreen)
    kapt(libs.hilt.compiler)

    // Unit tests
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.junit)

    // Instrumented tests
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)

    // Hilt instrumented tests
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler.test)

    implementation(libs.accompanist.navigation.animation)
    androidTestImplementation(libs.mockk.android)

}

