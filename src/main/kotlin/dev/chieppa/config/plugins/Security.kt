package dev.chieppa.config.plugins

import dev.chieppa.config.config
import dev.chieppa.controller.validateLogin
import dev.chieppa.util.LOGIN
import dev.chieppa.util.LOGIN_PASSWORD
import dev.chieppa.util.LOGIN_USERNAME
import dev.chieppa.controller.validateSession
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.sessions.*

const val USER_SESSION = "user_session"
const val AUTH_FORM = "auth-form"
const val AUTH_SESSION = "auth-session"
const val ADMIN_SESSION = "admin-session"

data class UserSession(val username: String, val sessionId: String) : Principal

fun Application.configureSecurity() {
    install(Sessions) {
        cookie<UserSession>(USER_SESSION) {
            cookie.path = "/"
            cookie.maxAgeInSeconds = config.users.sessionDuration
            cookie.secure = config.ktor.secureCookies
            cookie.extensions["SameSite"] = "lax"
        }
    }
    install(Authentication) {
        form(name = AUTH_FORM) {
            userParamName = LOGIN_USERNAME
            passwordParamName = LOGIN_PASSWORD
            validate {
                if (validateLogin(it.name, it.password)) {
                    UserIdPrincipal(it.name)
                } else {
                    null
                }
            }
        }
        session<UserSession>(AUTH_SESSION) {
            validate { validateSession(it) }
            challenge(LOGIN)
        }
    }
}

private fun validateSession(session: UserSession) =
    if (validateSession(session.username, session.sessionId)) {
        session
    } else {
        null
    }
