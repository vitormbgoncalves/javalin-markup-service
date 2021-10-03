
subprojects {
    repositories {
        mavenCentral()
    }

    dependencies {
        // Kotlin library
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.2-native-mt")
    }
}
