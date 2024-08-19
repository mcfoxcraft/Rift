plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.7.2"
    id("xyz.jpenilla.run-paper") version "2.1.0"
}

group = "com.volmit.rift"
version = "2.0.7"
val apiVersion = "1.21"

repositories {
    maven("https://maven.fabricmc.net/")
    maven("https://dl.cloudsmith.io/public/arcane/archive/maven/")
    mavenCentral()
    mavenLocal()
}

dependencies {
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

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
