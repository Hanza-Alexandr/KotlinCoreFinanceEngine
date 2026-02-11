plugins {
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.serialization") version "2.2.20"
    id("app.cash.sqldelight") version "2.0.2"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
sqldelight {
    databases {
        // Schema name
        create("Database") {
            // Package name
            packageName.set("com.example")
            dialect("app.cash.sqldelight:sqlite-3-35-dialect:2.0.2")

        }
    }
}


dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    implementation("app.cash.sqldelight:jdbc-driver:2.0.2")
    implementation("org.slf4j:slf4j-simple:2.0.9")
    implementation("app.cash.sqldelight:sqlite-driver:2.0.2")
    implementation("app.cash.sqldelight:sqlite-3-35-dialect:2.0.2")
    testImplementation(kotlin("test"))
}
tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaExec> {
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}

kotlin {
    jvmToolchain(24)
}