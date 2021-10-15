package dev.chieppa.controller

import dev.chieppa.config.config
import mu.KotlinLogging
import kotlin.concurrent.thread

private val logging = KotlinLogging.logger {  }

private var keepOff = false
private var keepOffThread: Thread? = null

fun getKeepOff() = keepOff

fun keepOffThread() = thread(start = false, isDaemon = true, name = "keepMonitorOff") {
    while (keepOff) {
        turnOffMonitor()
        Thread.sleep(config.monitor.keepOffRefresh.toLong())
    }
}

fun turnOffMonitor() {
    logging.info("turning off monitor")
}

fun keepMonitorOff() {
    if(keepOffThread?.isAlive == true) {
        logging.info("Monitor Already being kept off")
    } else {
        keepOff = false
        keepOffThread?.join()
        keepOffThread = keepOffThread()

        keepOff = true
        keepOffThread?.start()
    }
}

fun disableKeepOff() {
    keepOff = false
}