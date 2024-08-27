import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("androidx.navigation.safeargs")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.gamesuniverse"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.gamesuniverse"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    //libreria per collegamento firebase
    //implementation("com.google.firebase:firebase-firestore:24.9.1")
    implementation (platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation ("com.google.firebase:firebase-auth")
    implementation ("com.google.firebase:firebase-database")
    //libreria Glide per caricamento immagini
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.preference:preference:1.2.0")
    //implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("org.jsoup:jsoup:1.14.3")

    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    val nav_version = "2.7.6"

    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:$nav_version")
    implementation ("com.google.android.material:material:1.5.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation("commons-validator:commons-validator:1.7")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-base:18.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}