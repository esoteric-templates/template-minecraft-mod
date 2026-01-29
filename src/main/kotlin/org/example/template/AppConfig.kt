package org.example.template

import kotlinx.serialization.Serializable

@Serializable
data class AppConfig(var first: String = "Hello World!", var second: String = "Goodbye World!")
