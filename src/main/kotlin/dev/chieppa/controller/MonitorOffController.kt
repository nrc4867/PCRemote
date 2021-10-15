package dev.chieppa.controller

import com.profesorfalken.jpowershell.PowerShell
import dev.chieppa.config.config
import dev.chieppa.controller.MonitorOffController.script
import mu.KotlinLogging
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.concurrent.thread


private val logging = KotlinLogging.logger {  }

private object MonitorOffController {
    val script by lazy { MonitorOffController::class.java.getResource("/turn-off-display.ps1")?.readText() }
}
private val powerShell: PowerShell by lazy { PowerShell.openSession() }


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
    powerShell.executeCommand(script)
}

fun keepMonitorOff() {
    if(keepOffThread?.isAlive == true) {
        logging.info("Monitor Already being kept off")
    } else {
        keepOff = false
        keepOffThread?.interrupt()
        keepOffThread?.join()
        keepOffThread = keepOffThread()

        keepOff = true
        keepOffThread?.start()
    }
}

fun disableKeepOff() {
    keepOff = false
}