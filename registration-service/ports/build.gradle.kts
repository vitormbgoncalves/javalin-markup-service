
val joda_money_version: String by rootProject
val java_money_version: String by rootProject

dependencies {

    // Joda Money
    implementation("org.joda:joda-money:$joda_money_version")
}
repositories {
    mavenCentral()
}
