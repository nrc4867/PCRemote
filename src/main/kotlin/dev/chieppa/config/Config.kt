package dev.chieppa.config

import com.sksamuel.hoplite.ConfigLoader
import com.sksamuel.hoplite.Masked
import com.sksamuel.hoplite.PropertySource
import com.sksamuel.hoplite.fp.getOrElse
import java.util.*
import kotlin.io.path.Path

enum class Release{
    PROD, DEV
}

data class DatabaseConfig(
    val username: String,
    val password: Masked,
    val jdbcUrl: String,
    val maximumPoolSize: Int,
    val driverClassName: String
)

data class KtorServerConfig(
    val port: Int,
    val host: String,
    val secureCookies: Boolean
)

data class Users(val pepper: Masked, val sessionDuration: Long, val enableNewAccounts: Boolean, val minPasswordLength: Int, val maxPasswordLength: Int)

data class Bcrypt(val saltrounds: Int)

data class Monitor(val keepOffRefresh: Int)

data class Configuration(
    val release: Release,
    val database: DatabaseConfig,
    val ktor: KtorServerConfig,
    val users: Users,
    val bcrypt: Bcrypt,
    val monitor: Monitor
)

data class ArgsConfiguration(val config: String)

private var args: Array<String> = arrayOf()
val config: Configuration by lazy {
    if (args.isEmpty()) {
        args = getProperty("args").split(" ").toTypedArray()
    }

    val argsParser = ConfigLoader.Builder()
        .addSource(PropertySource.commandLine(args))
        .build()
        .loadConfig<ArgsConfiguration>()

    ConfigLoader.Builder()
        .addSource(PropertySource.path(Path(argsParser.getOrElse { ArgsConfiguration("") }.config), true))
        .addSource(PropertySource.resource("/default-config.yaml"))
        .build()
        .loadConfigOrThrow()
}

fun setupConfig(runningArgs: Array<String>) {
    args = runningArgs
    config
}

private fun getProperty(name: String, default: String = "") : String =
    System.getProperty("dev.chieppa.emailtracker.$name", default)


