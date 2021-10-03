val kotlinLanguage_version: String by rootProject
val joda_money_version: String by rootProject
val java_money_version: String by rootProject
val jackson_version: String by rootProject
val javax_el_version: String by project
val hibernate_validator_version: String by rootProject
val koin_version: String by rootProject
val typesafe_version: String by rootProject
val hikaricp_version: String by rootProject
val postgresql_version: String by rootProject
val exposed_version: String by rootProject
val javalin_version: String by rootProject
val log4j_version: String by rootProject
val jetty_version: String by rootProject
val kafka_client_version: String by rootProject

plugins {
    kotlin("kapt")
}

dependencies {

    // Projects dependencies
    implementation(project(":markup-service:ports"))
    implementation(project(":markup-service:core"))

    // Joda Money
    implementation("org.joda:joda-money:$joda_money_version")

    // Javalin Framework
    implementation("io.javalin:javalin:$javalin_version")

    // Jetty
    implementation("org.eclipse.jetty:jetty-server:$jetty_version")

    // Javalin Plugins
    kapt("io.javalin-rfc:openapi-annotation-processor:1.1.1")
    implementation("com.reposilite.javalin-rfcs:javalin-context:4.0.11")
    implementation("com.reposilite.javalin-rfcs:javalin-reactive-routing:4.0.11")
    implementation("io.javalin-rfc:javalin-openapi-plugin:1.1.1")
    implementation("io.javalin-rfc:javalin-swagger-plugin:1.1.1")
    implementation("io.javalin-rfc:javalin-redoc-plugin:1.1.1")

    // Koin dependency injection
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    testImplementation("io.insert-koin:koin-test:$koin_version")

    // Jackson Serialization
    implementation("com.fasterxml.jackson.core:jackson-core:$jackson_version")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jackson_version")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jackson_version")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jackson_version")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-joda-money:$jackson_version")

    // Log4j2 Logging
    implementation("org.apache.logging.log4j:log4j-core:$log4j_version")
    implementation("org.apache.logging.log4j:log4j-api:$log4j_version")
/*    implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4j_version")*/

    // Validation
    implementation("org.glassfish:javax.el:$javax_el_version")
    implementation("org.hibernate.validator:hibernate-validator:$hibernate_validator_version")

    // HOCON configuration library
    implementation("com.typesafe:config:$typesafe_version")

    // HikariCP
    implementation("com.zaxxer:HikariCP:$hikaricp_version")
    implementation("org.postgresql:postgresql:$postgresql_version")

    // Exposed ORM
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-money:$exposed_version")

    // Kafka client Kotlin
    implementation("io.streamthoughts:kafka-clients-kotlin:$kafka_client_version")
}

tasks {
    kapt {
        includeCompileClasspath = false
    }
}
repositories {
    mavenCentral()
}
