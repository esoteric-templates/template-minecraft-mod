package org.example.template

import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object TemplateMod : ModInitializer {
	const val MOD_ID = "template"

	val logger: Logger = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		logger.info("Hello Fabric world!")
	}
}
