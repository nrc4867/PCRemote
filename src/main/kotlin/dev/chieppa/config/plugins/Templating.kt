package dev.chieppa.config.plugins

import dev.chieppa.config.Release.DEV
import dev.chieppa.config.config
import io.ktor.application.*
import io.ktor.thymeleaf.*
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

fun Application.configureTemplating() {
    install(Thymeleaf) {
        setTemplateResolver(ClassLoaderTemplateResolver().apply {
            prefix = "templates/thymeleaf/"
            suffix = ".html"
            characterEncoding = "utf-8"
            if (config.release == DEV) {
                cacheTTLMs = 0
            }
        })
    }
}