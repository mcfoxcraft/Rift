plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.1.0"
}

group = "com.volmit.rift"
version = "2.0.7"
val apiVersion = "1.21"

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
}

tasks {
    processResources {
        filesMatching("**/plugin.yml") {
            expand(
                "version" to version,
                "apiversion" to apiVersion
            )
        }
    }

    runServer {
        minecraftVersion("1.21.1")
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}
