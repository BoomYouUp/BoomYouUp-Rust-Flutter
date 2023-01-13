pluginManagement {
    repositories {
        google()
        mavenCentral()
    }

    plugins {
        kotlin("android").version(extra["kotlin.version"] as String)
        id("com.android.application") version "7.3.1"
    }
}

include(":app")

val properties = java.util.Properties()
val localPropertiesFile = file("local.properties")

assert(localPropertiesFile.exists())

localPropertiesFile.reader().use { reader -> properties.load(reader) }

val flutterSdkPath = properties.getProperty("flutter.sdk")
assert(flutterSdkPath != null)

apply("$flutterSdkPath/packages/flutter_tools/gradle/app_plugin_loader.gradle")
