package dev.chieppa.config.plugins

import dev.chieppa.util.HOME
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import mu.KLogger
import mu.KotlinLogging

fun Application.configureRouting() {
    routing {
        install(StatusPages) {
            exception<AuthenticationException> {
                call.respondRedirect(HOME)
            }
            exception<AuthorizationException> {
                call.respondRedirect(HOME)
            }
        }
        static("/") {
            resources(resourcePackage = "/assets")
            file("/assets/favicon.ico")
        }
        static("assets") {
            resources(resourcePackage = "/assets")
            files("scripts")
            files("images")
            files("stylesheets")
        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()

private val logger: KLogger by lazy { KotlinLogging.logger { } }

fun Application.ktorDevelopment() {
    // set up omitted
    val root = feature(Routing)
    val allRoutes = allRoutes(root)
    val allRoutesWithMethod = allRoutes.filter { it.selector is HttpMethodRouteSelector }
    allRoutesWithMethod.forEach {
        logger.info("route: $it")
    }
}

fun allRoutes(root: Route): List<Route> {
    return listOf(root) + root.children.flatMap { allRoutes(it) }
}