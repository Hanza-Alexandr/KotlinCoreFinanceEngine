plugins {
    kotlin("jvm") version "2.2.20"
    id("app.cash.sqldelight") version "2.0.0"
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
        }
    }
}


dependencies {
    implementation("app.cash.sqldelight:sqlite-driver:2.0.0")
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