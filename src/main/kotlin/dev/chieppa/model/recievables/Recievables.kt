package dev.chieppa.model.recievables

import kotlinx.serialization.Serializable

@Serializable
data class Login(val username: String?, val password: String?)

@Serializable
data class MonitorState(val state: Boolean?)