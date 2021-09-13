rootProject.name = "javalin-markup-service"

pluginManagement {

    val kotlinPluginVersion: String by settings
    val ktlintPluginVersion: String by settings
    val detektPluginVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinPluginVersion
        kotlin("kapt") version kotlinPluginVersion
        id("org.jlleitschuh.gradle.ktlint") version ktlintPluginVersion
        id("io.gitlab.arturbosch.detekt") version detektPluginVersion
    }
}

