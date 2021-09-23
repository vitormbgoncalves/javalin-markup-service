rootProject.name = "javalin-markup-service"

include("ports")
include("core")
include("adapters")
include("app")

pluginManagement {

    val kotlinPluginVersion: String by settings
    val ktlintPluginVersion: String by settings
    val detektPluginVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinPluginVersion
        id("org.jlleitschuh.gradle.ktlint") version ktlintPluginVersion
        id("io.gitlab.arturbosch.detekt") version detektPluginVersion
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "kotlin2js") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
            }
        }
    }
}

