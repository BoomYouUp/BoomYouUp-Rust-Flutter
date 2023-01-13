import java.util.*

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.reader().use { reader ->
        localProperties.load(reader)
    }
}

val flutterRoot: String = localProperties.getProperty("flutter.sdk")
    ?: throw GradleException("Flutter SDK not found. Define location with flutter.sdk in the local.properties file.")
val flutterVersionCode: String = localProperties.getProperty("flutter.versionCode") ?: "1"
var flutterVersionName: String = localProperties.getProperty("flutter.versionName") ?: "0.0.0"

plugins {
    id("com.android.application")
    kotlin("android")
}

apply("$flutterRoot/packages/flutter_tools/gradle/flutter.gradle")

val signingProperties = Properties()
val signingPropertiesFile = rootProject.file("key.properties")
if (signingPropertiesFile.exists()) {
    signingPropertiesFile.reader().use { reader ->
        signingProperties.load(reader)
    }
}

android {
    namespace = "xyz.xfqlittlefan.boomyouup"
    compileSdk = 33 //flutter.compileSdkVersion
    // ndkVersion flutter.ndkVersion

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    defaultConfig {
        applicationId = "xyz.xfqlittlefan.boomyouup"
        // You can update the following values to match your application needs.
        // For more information, see: https://docs.flutter.dev/deployment/android#reviewing-the-build-configuration.
        minSdk = 24 //flutter.minSdkVersion
        targetSdk = 33 //flutter.targetSdkVersion
        versionCode = flutterVersionCode.toInt()
        versionName = flutterVersionName
    }

    signingConfigs {
        create("config") {
            storeFile = rootProject.file("key.jks")
            storePassword = signingProperties.getProperty("b")
            keyAlias = signingProperties.getProperty("a")
            keyPassword = signingProperties.getProperty("b")
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs["config"]
        }
    }
}

project.extensions["flutter"].apply {
    this::class.java.getMethod("source", String::class.java).invoke(this, "../..")
}

dependencies {
    //implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${rootProject.extra["kotlin.version"]}")
}

mapOf(
    "Debug" to null, "Profile" to "--release", "Release" to "--release"
).forEach { (taskPostfix, profileMode) ->
    tasks.whenTaskAdded {
        if (name == "javaPreCompile$taskPostfix") {
            dependsOn("cargoBuild$taskPostfix")
        }
    }
    tasks.register("cargoBuild$taskPostfix", Exec::class.java) {
        workingDir = file("../../native")
        environment = mapOf("ANDROID_NDK_HOME" to localProperties.getProperty("ndk.dir"))
        commandLine = listOf(
            "cargo",
            "ndk",
            "-t",
            "armeabi-v7a",
            "-t",
            "arm64-v8a",
            "-t",
            "x86",
            "-t",
            "x86_64",
            "-o",
            "../android/app/src/main/jniLibs",
            "build"
        )
        if (profileMode != null) {
            setArgs(listOf(profileMode))
        }
    }
}
