plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.1"
}

group = "com.carlos.plugins.tst"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    testCompileOnly("org.projectlombok:lombok:1.18.34")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.34")

    implementation("com.zaxxer:HikariCP:4.0.3")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {

    withType(JavaCompile::class).configureEach {
        options.encoding = "UTF-8"
    }

    shadowJar {
        mergeServiceFiles()

        archiveFileName.set("Home.jar")
        destinationDirectory.set(file("C:/GameDev/Server_1-21/plugins"))

        dependencies {
            exclude(dependency("org.yaml:snakeyaml"))
        }

    }


}

