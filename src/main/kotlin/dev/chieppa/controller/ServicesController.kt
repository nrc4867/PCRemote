package dev.chieppa.controller

import dev.chieppa.model.response.ServiceComplete
import dev.chieppa.util.createPath
import mu.KotlinLogging
import java.io.File

val logger = KotlinLogging.logger { }

val system32directory = listOf("C:", "Windows", "System32")

fun startSSH(): ServiceComplete {
    val service = startService(
        listOf("wsl.exe", "--user", "root", "service", "ssh", "start"),
        workingDirectory = system32directory
    )

    return ServiceComplete(consoleOutput = service.first, exitCode = service.second)
}


fun startService(
    command: List<String>,
    env: List<String> = listOf(),
    workingDirectory: List<String>
): Pair<String, Int> {
    val runtime = Runtime.getRuntime()
    val process = runtime.exec(
        command.toTypedArray(),
        env.toTypedArray(),
        File(workingDirectory.createPath())
    )
    val output = process.inputStream.use {
        process.waitFor()
        it.reader().use { reader -> reader.readText() }
    }

    logger.info { "Process exited with value: ${process.exitValue()}\n$output" }

    return Pair(output, process.exitValue())
}