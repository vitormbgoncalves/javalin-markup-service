val javalin_version: String by rootProject
val typesafe_version: String by rootProject
val koin_version: String by rootProject

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

dependencies {
    // Project dependencies
    implementation(project(":registration-service:ports"))
    implementation(project(":registration-service:adapters"))
    implementation(project(":registration-service:core"))

    // HOCON configuration library
    implementation("com.typesafe:config:$typesafe_version")

    // Koin dependency injection
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    testImplementation("io.insert-koin:koin-test:$koin_version")
}

repositories {
    mavenCentral()
}
