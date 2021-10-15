package dev.chieppa.controller

import dev.chieppa.model.response.ServiceComplete
import dev.chieppa.util.createPath

fun startSSH(): ServiceComplete {
    val runtime = Runtime.getRuntime()
    val process = runtime.exec(listOf("C:", "Windows", "System32", "wsl.exe").createPath() + " --user root service ssh start")
    process.waitFor()
    return ServiceComplete(exitCode = process.exitValue())
}