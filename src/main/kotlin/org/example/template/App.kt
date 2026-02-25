package org.example.template

import kotlinx.serialization.json.Json
import java.io.File

class App(private val config: AppConfig = AppConfig()) {
	val greeting: String
		get() {
			return config.first
		}

	val farewell: String
		get() {
			return config.second
		}
}

fun main() {
	val serializer = Json { encodeDefaults = true; prettyPrint = true; prettyPrintIndent = "\t"; }
	val configs = File(System.getenv("XDG_CONFIG_HOME")
		?: System.getenv("HOME")?.run { "$this/.config" }
		?: System.getenv("USER")?.run { "/home/$this/.config"}
		?: "~/.config")

	val configDir = configs.resolve(App::class.java.simpleName.lowercase()).apply { this.mkdirs() }

	val config: AppConfig
	val settings = configDir.resolve("settings.json")

	if (!settings.exists()) {
		settings.createNewFile()

		config = AppConfig()
		val json = serializer.encodeToString(config)

		settings.writeText("${json}\n")
	} else {
		val json = settings.readText()
		config = serializer.decodeFromString<AppConfig>(json)
	}

	val app = App(config)

	println(app.greeting)
	println(app.farewell)
}
