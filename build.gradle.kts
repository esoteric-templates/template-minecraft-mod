import org.apache.batik.transcoder.SVGAbstractTranscoder
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import java.util.jar.Attributes

buildscript {
	repositories {
		mavenCentral()
	}

	dependencies {
		classpath(libs.batik.transcoder)
		classpath(libs.batik.codec)
	}
}

plugins {
	alias(libs.plugins.kotlin)
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.loom)
}

repositories {
	mavenCentral()
}

dependencies {
	minecraft(libs.minecraft)

	implementation(libs.fabric.loader)
	implementation(libs.fabric.api)

	implementation(libs.fabric.kotlin)

	implementation(libs.kotlin.serialization)
}

group = "org.example"
description = "Template Minecraft Fabric mod"

version = ProcessBuilder("git", "describe", "--tags", "--always", "--dirty")
	.directory(project.projectDir)
	.start()
	.inputStream
	.bufferedReader()
	.readText()
	.trim()

loom {
	splitEnvironmentSourceSets()

	mods {
		register(project.name) {
			sourceSet("main")
			sourceSet("client")
		}
	}

	log4jConfigs.from("conf/log.xml")
}

val generatedResources: Directory = layout.buildDirectory.dir("generated/resources").get()

sourceSets.main {
	resources {
		exclude("assets/${project.name}/icon.svg")
		srcDir(generatedResources)
	}
}

tasks {
	val generateIcon = register("generateIcon") {
		val inputFile = file("src/main/resources/assets/${project.name}/icon.svg")
		val outputFile = generatedResources.file("assets/${project.name}/icon.png").asFile

		inputs.file(inputFile)
		outputs.file(outputFile)

		doLast {
			outputFile.parentFile.mkdirs()

			val transcoder = PNGTranscoder()
			val sizePixels = 128F

			transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, sizePixels)
			transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, sizePixels)

			val input = TranscoderInput(inputFile.toURI().toString())
			val output = TranscoderOutput(outputFile.outputStream())

			transcoder.transcode(input, output)
		}
	}

	processResources {
		filesMatching("fabric.mod.json") {
			expand(
				mapOf(
					"name" to project.name,
					"group" to project.group,
					"description" to project.description,

					"minecraft_version" to libs.versions.minecraft.get(),
					"fabric_version" to libs.versions.fabric.loader.get(),
					"fabric_kotlin_version" to libs.versions.fabric.kotlin.get(),
					"fabric_api_version" to libs.versions.fabric.api.get(),
					"java_version" to JavaVersion.current().majorVersion.toInt(),
					"version" to project.version
				)
			)
		}

		dependsOn(generateIcon)
	}

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

	withType<Jar> {
		manifest {
			attributes[Attributes.Name.IMPLEMENTATION_TITLE.toString()] = "Template Minecraft Fabric Mod"
			attributes[Attributes.Name.IMPLEMENTATION_VERSION.toString()] = project.version
			attributes[Attributes.Name.IMPLEMENTATION_VENDOR.toString()] = "Дима Ш."
		}
	}
}

listOf(tasks.jar, tasks.kotlinSourcesJar).forEach {
	it {
		into("META-INF") {
			from("LICENSE.txt")
			from("NOTICE.txt")
			from("docs/DISCLAIMER.txt")
		}
	}
}

configurations.all {
	resolutionStrategy {
		failOnNonReproducibleResolution()
	}
}
