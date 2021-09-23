val koin_version: String by rootProject

dependencies {

    // Project dependencies
    implementation(project(":ports"))

    // Koin dependency injection
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    testImplementation("io.insert-koin:koin-test:$koin_version")
}
repositories {
    mavenCentral()
}