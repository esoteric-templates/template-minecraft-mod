//import java.util.jar.Attributes

plugins {
    alias(libs.plugins.kotlin)
//    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.kotlin.test)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

group = "org.example"

//application {
//    mainClass = "$group.$name.AppKt"
//}

version = ProcessBuilder("git", "describe", "--tags", "--always", "--dirty")
    .directory(project.projectDir)
    .start()
    .inputStream
    .bufferedReader()
    .readText()
    .trim()

tasks {
    withType<AbstractArchiveTask> {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true

        filePermissions {
            user.read = true
            user.write = true
            user.execute = false

            group.read = true
            group.write = false
            group.execute = false

            other.read = true
            other.write = false
            other.execute = false
        }

        dirPermissions {
            user.read = true
            user.write = true
            user.execute = true

            group.read = true
            group.write = false
            group.execute = true

            other.read = false
            other.write = false
            other.execute = true
        }
    }

//    withType<Jar> {
//        manifest {
//            attributes[Attributes.Name.MAIN_CLASS.toString()] = application.mainClass
//        }
//    }

    test {
        useJUnitPlatform()
    }
}

//listOf(tasks.distTar, tasks.distZip).forEach {
//    it {
//        from("assets/text/licenses/") {
//            into("licenses/")
//        }
//    }
//}

listOf(tasks.jar, tasks.kotlinSourcesJar).forEach {
    it {
        from("LICENSE")
        from("NOTICE")
    }
}

configurations.all {
    resolutionStrategy {
        failOnNonReproducibleResolution()
    }
}
