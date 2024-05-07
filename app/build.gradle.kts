plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.prototypea"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.prototypea"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        viewBinding = true
    }
}
dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("me.grantland:autofittextview:0.2.1")


    implementation ("com.squareup.picasso:picasso:2.8")
    //MPAndroidChar
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    //gif
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.28")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    //anh tron
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    //add ZXing
    implementation("com.google.zxing:core:3.4.1")
    implementation("com.journeyapps:zxing-android-embedded:4.2.0")
//    implementation(libs.firebase.messaging)
    implementation("com.google.firebase:firebase-messaging:21.1.0")
    implementation("com.google.firebase:firebase-inappmessaging:20.0.0")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
}