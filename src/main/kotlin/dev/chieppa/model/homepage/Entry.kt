package dev.chieppa.model.homepage

import kotlinx.serialization.Serializable

@Serializable
data class Entry(val name: String, val linkaddr: String)
