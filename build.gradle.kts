import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jlleitschuh.gradle.ktlint.KtlintExtension

val kotlinLanguage_version: String by project
val stdlib_version: String by project
val joda_money_version: String by project
val java_money_version: String by project
val jackson_version: String by project
val javax_el_version: String by project
val hibernate_validator_version: String by project
val koin_version: String by project
val typesafe_version: String by project
val hikaricp_version: String by project
val exposed_version: String by project
val javalin_version: String by project

val log4j_version: String by project
val kotest_version: String by project
val mockk_version: String by project
val testcontainers_version: String by project

plugins {
    kotlin("jvm")
    kotlin("kapt")
    java
    jacoco
    id("org.jlleitschuh.gradle.ktlint")
    id("io.gitlab.arturbosch.detekt")
}

group = "com.github.vitormbgoncalves"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.panda-lang.org/releases")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$stdlib_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2-native-mt")

    // Joda Money
    implementation("org.joda:joda-money:$joda_money_version")

    // Javalin Framework
    implementation("io.javalin:javalin:$javalin_version")

    // Javalin Plugins
    implementation("com.reposilite.javalin-rfcs:javalin-context:4.0.11")
    implementation("com.reposilite.javalin-rfcs:javalin-reactive-routing:4.0.11")
    kapt("io.javalin-rfc:openapi-annotation-processor:1.1.0")
    implementation("io.javalin-rfc:javalin-openapi-plugin:1.1.0")
    implementation("io.javalin-rfc:javalin-swagger-plugin:1.1.0")

    // Koin dependency injection
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    testImplementation("io.insert-koin:koin-test:$koin_version")

    // Jackson Serialization
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jackson_version")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-joda-money:$jackson_version")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jackson_version")

    // Log4j2 Logging
    implementation("org.apache.logging.log4j:log4j-core:$log4j_version")
    implementation("org.apache.logging.log4j:log4j-api:$log4j_version")

    // Validation
    implementation("org.glassfish:javax.el:$javax_el_version")
    implementation("org.hibernate.validator:hibernate-validator:$hibernate_validator_version")

    // HOCON configuration library
    implementation("com.typesafe:config:$typesafe_version")

    // HikariCP
    implementation("com.zaxxer:HikariCP:$hikaricp_version")

    // Exposed ORM
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-money:$exposed_version")
    implementation("org.javamoney:moneta:$java_money_version")

    // Test
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotest_version")
    testImplementation("io.kotest:kotest-assertions-core:$kotest_version")
    testImplementation("io.kotest:kotest-property:$kotest_version")
    testImplementation("io.mockk:mockk:$mockk_version")
    implementation("io.kotest:kotest-extensions-testcontainers-jvm:$testcontainers_version")
}

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
