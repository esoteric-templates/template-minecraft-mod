package org.example.template

import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TemplateMod : ModInitializer {
	companion object {
		const val MOD_ID = "template"
		val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
	}

	override fun onInitialize() {
		LOGGER.info("Hello Fabric world!")
	}
}
