plugins {
    kotlin("jvm") version "1.8.20" apply true
    id("io.papermc.paperweight.userdev") version "1.5.5"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "encryptsl.cekuj.net"
version = providers.gradleProperty("plugin_version").get()
description = providers.gradleProperty("plugin_description").get()

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
    maven {
        url = uri("https://repo.codemc.io/repository/maven-public/")
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    paperweight.paperDevBundle(providers.gradleProperty("server_version").get())
    compileOnly(kotlin("stdlib", "1.8.20"))
    compileOnly("me.lokka30:treasury-api:2.0.0")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7") {
        exclude("org.bukkit", "bukkit")
    }
    compileOnly("com.zaxxer:HikariCP:5.0.1")
    compileOnly("me.clip:placeholderapi:2.11.3")
    compileOnly("org.jetbrains.exposed:exposed-core:0.40.1")
    compileOnly("org.jetbrains.exposed:exposed-jdbc:0.40.1")
    compileOnly("com.squareup.okhttp3:okhttp:4.10.0")

    implementation("org.bstats:bstats-bukkit:3.0.0")
    implementation("cloud.commandframework:cloud-paper:1.8.3")
    implementation("cloud.commandframework:cloud-annotations:1.8.3")

    testImplementation(kotlin("test"))
    testImplementation("com.zaxxer:HikariCP:5.0.1")
    testImplementation("org.xerial:sqlite-jdbc:3.40.0.0")
    testImplementation("org.jetbrains.exposed:exposed-core:0.40.1")
    testImplementation("org.jetbrains.exposed:exposed-jdbc:0.40.1")
    testImplementation("com.squareup.okhttp3:okhttp:4.10.0")
}

tasks {

    build {
        dependsOn(shadowJar)
    }

    test {
        useJUnitPlatform()
    }

    processResources {
        filesMatching("plugin.yml") {
            expand(project.properties)
        }
    }

    reobfJar {
        outputJar.set(layout.buildDirectory.file("libs/${providers.gradleProperty("plugin_name").get()}-${version}.jar"))
    }

    shadowJar {
        minimize {
            relocate("org.bstats", "encryptsl.cekuj.net.api.bstats")
            relocate("cloud.commandframework", "encryptsl.cekuj.net.cloud")
        }
    }
}