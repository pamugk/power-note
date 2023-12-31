import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "com.github.pamugk"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

val exposedVersion: String by project
dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)
    implementation(compose.material3)
    implementation("com.mohamedrejeb.richeditor:richeditor-compose:1.0.0-beta05")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
    implementation("org.slf4j:slf4j-nop:2.0.9")
    implementation("org.xerial:sqlite-jdbc:3.44.1.0")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        buildTypes.release.proguard {
            configurationFiles.from(
                project.file("kotlinx-datetime-serialization.pro"),
                project.file("kotlinx-serialization.pro")
            )
        }
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.AppImage)
            copyright = "© pamugk 2023. No rights reserved"
            description = "PowerNote — программа для управления заметками"
            licenseFile = file("LICENSE")
            packageName = "power-note"
            packageVersion = "1.0.0"

            modules("java.sql")
            linux {
                iconFile = file("src/main/resources/icon_main.png")
            }
            windows {
                iconFile = file("src/main/resources/icon_main.ico")
            }
        }
    }
}
