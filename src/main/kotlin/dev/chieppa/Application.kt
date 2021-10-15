package dev.chieppa

import dev.chieppa.config.connectToDatabase
import dev.chieppa.config.startServer
import dev.chieppa.config.setupConfig

fun main(args: Array<String>) {
    setupConfig(args)
    connectToDatabase()
    startServer()
}