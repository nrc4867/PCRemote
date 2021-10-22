package dev.chieppa.view

import dev.chieppa.config.plugins.AUTH_FORM
import dev.chieppa.config.plugins.AUTH_SESSION
import dev.chieppa.config.plugins.UserSession
import dev.chieppa.model.homepage.Entry
import dev.chieppa.util.*
import dev.chieppa.config.config
import dev.chieppa.config.plugins.jsonFormat
import dev.chieppa.controller.*
import dev.chieppa.model.exception.ExceptionType
import dev.chieppa.model.recievables.Login
import dev.chieppa.model.recievables.MonitorState
import dev.chieppa.model.response.ErrorResponse
import dev.chieppa.model.response.InformationalResponse
import dev.chieppa.model.response.MonitorStateChanged
import dev.chieppa.model.response.SuccessResponse
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.thymeleaf.*
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

fun Application.createHomeRoute() {
    routing {
        get(HOME) {
            if (call.sessions.get<UserSession>() != null) {
                call.respondRedirect(PC_CONTROL)
            } else {
                logger.info { "Connection from ${call.request.local.host}" }
                call.respond(
                    ThymeleafContent(
                        "homepage",
                        mapOf("new_accounts_enabled" to config.users.enableNewAccounts)
                    )
                )
            }
        }
    }
}

fun Application.createUserRoute() {
    routing {
        post(CREATE_ACCOUNT) {
            val user = call.receive<Login>()

            if (userExits(user.username.orEmpty())) {
                call.respond(
                    ErrorResponse(
                        errorType = ExceptionType.ACCOUNT_EXISTS,
                        error = jsonFormat.encodeToJsonElement(
                            InformationalResponse(
                                message = "The account could not be created because an account with this username already exists"
                            )
                        )
                    )
                )
                return@post
            }

            if (createUser(user.username.orEmpty(), user.password.orEmpty())) {
                call.sessions.set(createSession(user.username.orEmpty()))
                call.respond(SuccessResponse())
            } else {
                call.respond(
                    ErrorResponse(
                        errorType = ExceptionType.ACCOUNT_NOT_CREATED,
                        error = jsonFormat.encodeToJsonElement(InformationalResponse(message = "Account could not be created"))
                    )
                )
            }
        }
    }
}

fun Application.createLoginRoute() {
    routing {
        post(LOGIN) {
            val user = call.receive<Login>()
            if (validateLogin(user.username.orEmpty(), user.password.orEmpty())) {
                val session = createSession(user.username.orEmpty())
                call.sessions.set(session)
                call.respond(SuccessResponse())
            } else {
                call.respond(
                    ErrorResponse(
                        errorType = ExceptionType.ACCOUNT_DNE,
                        error = jsonFormat.encodeToJsonElement(InformationalResponse(message = "Account Does Not Exist"))
                    )
                )
            }
        }

        get(LOGOUT) {
            call.sessions.get<UserSession>()?.let { deleteSession(it) }
            call.sessions.clear<UserSession>()
            call.respondRedirect(HOME)
        }
    }
}

fun Application.createControlRoute() {
    routing {
        authenticate(AUTH_SESSION) {
            get(PC_CONTROL) {
                val user = call.sessions.get<UserSession>()!!
                call.respond(
                    ThymeleafContent(
                        "control",
                        mapOf(
                            "username" to user.username,
                            "controls" to listOf("monitor", "services", "shutdown"),
                            "keepOnState" to getKeepOff()
                        )
                    )
                )
            }
        }
    }
}

fun Application.createMonitorControlApi() {

    suspend fun changeMonitorState(call: ApplicationCall) {
        val monitorState = call.receive<MonitorState>()
        if (monitorState.state == true) {
            keepMonitorOff()
        } else {
            disableKeepOff()
        }
        call.respond(jsonFormat.encodeToJsonElement(MonitorStateChanged(state = MonitorState(getKeepOff()))))
    }

    routing {
        authenticate(AUTH_SESSION) {
            post(MONITOR_CONTROL) {
                val session = call.sessions.get<UserSession>()!!
                val command = call.parameters["command"]

                logger.info("'${session.username}' called monitor with '$command'")

                when (command) {
                    "turnoff" -> {
                        turnOffMonitor()
                        call.respond(SuccessResponse())
                    }
                    "keepoff" -> changeMonitorState(call)
                }
            }
        }
    }
}

fun Application.createServicesControlApi() {
    routing {
        authenticate(AUTH_SESSION) {
            post(SERVICES_CONTROL) {
                val session = call.sessions.get<UserSession>()!!
                val command = call.parameters["command"]

                logger.info("'${session.username}' called services with '$command'")

                when (command) {
                    "startSSH" -> call.respond(startSSH())
                }
            }
        }
    }
}

fun Application.createPowerControlsAPI() {
    routing {
        authenticate(AUTH_SESSION) {
            post(POWER_CONTROL) {
                val session = call.sessions.get<UserSession>()!!
                val command = call.parameters["command"]

                logger.info("'${session.username}' called services with '$command'")

                when(command) {
                    "lock" -> call.respond(lockPC())
                }
            }
        }
    }
}