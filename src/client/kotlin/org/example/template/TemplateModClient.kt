package org.example.template

import net.fabricmc.api.ClientModInitializer

class TemplateModClient : ClientModInitializer {
	override fun onInitializeClient() {
		TemplateMod.LOGGER.info("Hello Fabric client world!")
	}
}
