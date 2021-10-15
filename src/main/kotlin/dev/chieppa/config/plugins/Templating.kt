package dev.chieppa.config.plugins

import io.ktor.thymeleaf.Thymeleaf
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import io.ktor.application.*
import io.ktor.http.content.*
import org.thymeleaf.context.IWebContext
import org.thymeleaf.standard.expression.StandardExpressionExecutionContext
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateparser.markup.decoupled.IDecoupledTemplateLogicResolver
import org.thymeleaf.util.PatternSpec

fun Application.configureTemplating() {
    install(Thymeleaf) {
        setTemplateResolver(ClassLoaderTemplateResolver().apply {
            prefix = "templates/thymeleaf/"
            suffix = ".html"
            characterEncoding = "utf-8"
            cacheTTLMs = 0
            isCacheable = false
        })
    }
}