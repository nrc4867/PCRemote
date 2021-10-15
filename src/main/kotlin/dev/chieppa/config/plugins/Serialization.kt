package dev.chieppa.config.plugins

import io.ktor.serialization.*
import io.ktor.features.*
import io.ktor.application.*
import kotlinx.serialization.json.Json

val jsonFormat = Json { encodeDefaults = true }

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}