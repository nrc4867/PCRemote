package dev.chieppa.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

fun connectToDatabase() {
    val databaseConfig = HikariConfig().apply {
        jdbcUrl = config.database.jdbcUrl
        driverClassName = config.database.driverClassName
        username = config.database.username
        password = config.database.password.value
        maximumPoolSize = config.database.maximumPoolSize
    }
    val datasource = HikariDataSource(databaseConfig)
    Database.connect(datasource)
}