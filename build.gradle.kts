
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jlleitschuh.gradle.ktlint.KtlintExtension

val kotlinLanguage_version: String by project
val stdlib_version: String by project
val kotest_version: String by project
val mockk_version: String by project
val testcontainers_version: String by project

plugins {
    application
    kotlin("jvm")
    jacoco
    id("org.jlleitschuh.gradle.ktlint")
    id("io.gitlab.arturbosch.detekt")
}

allprojects {

    repositories {
        mavenCentral()
        maven { url = uri("https://repo.panda-lang.org/releases") }
        maven { url = uri("https://s01.oss.sonatype.org/content/repositories/releases/") }
        maven { url = uri("https://jitpack.io") }
    }

    apply(plugin = "application")
    apply(plugin = "kotlin")
    apply(plugin = "jacoco")
    apply(plugin = "kotlin-kapt")

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

subprojects {

    dependencies {

        // Test
        testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotest_version")
        testImplementation("io.kotest:kotest-assertions-core:$kotest_version")
        testImplementation("io.kotest:kotest-property:$kotest_version")
        testImplementation("io.mockk:mockk:$mockk_version")
        implementation("io.kotest:kotest-extensions-testcontainers-jvm:$testcontainers_version")
    }

    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    tasks {
        withType<KotlinCompile<*>> {
            kotlinOptions {
                languageVersion = kotlinLanguage_version
                apiVersion = kotlinLanguage_version
                (this as KotlinJvmOptions).jvmTarget =
                    JavaVersion.VERSION_1_8.toString()
                freeCompilerArgs = listOfNotNull(
                    "-Xopt-in=kotlin.RequiresOptIn"
                )
            }
        }

        withType<Test> {
            useJUnitPlatform()
        }

        withType<Detekt>().configureEach {
            jvmTarget = "1.8"
        }

        test {
            finalizedBy(jacocoTestReport)
        }

        jacocoTestReport {
            dependsOn(test)
            reports {
                xml.required.set(false)
                csv.required.set(false)
            }
        }

        configure<KtlintExtension> {
            debug.set(true)
            outputToConsole.set(true)
            outputColorName.set("RED")
        }

        detekt {
            config = files("config/detekt/detekt.yml")
            buildUponDefaultConfig = true

            reports {
                html.enabled = true
                xml.enabled = false
                txt.enabled = false
                sarif.enabled = false
            }
        }
    }
}
