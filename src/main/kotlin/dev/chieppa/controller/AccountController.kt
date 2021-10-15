package dev.chieppa.controller

import dev.chieppa.config.model.table.SessionIDTable
import dev.chieppa.config.model.table.UserTable
import dev.chieppa.util.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun validateSession(username: String, sessionID: String): Boolean {
    return transaction {
        addLogger(StdOutSqlLogger)

        SessionIDTable.select {
            SessionIDTable.userName eq username and (SessionIDTable.sessionId eq sessionID)
        }.firstNotNullOfOrNull {
            return@transaction true
        }
        false
    }
}

fun createUser(username: String, password: String): Boolean {
    if (!validateUsername(username)) return false
    if (!validatePassword(password)) return false
    val constrainedUsername = constrainUsername(username)
    return transaction {
        addLogger(StdOutSqlLogger)

        UserTable.insert {
            it[UserTable.userName] = constrainedUsername
            it[UserTable.password] = hashLongPassword(password)
        }
        true
    }
}

fun validateLogin(username: String, password: String): Boolean {
    if (!validateUsername(username)) return false
    val constrainedUsername = constrainUsername(username)
    return transaction {
        addLogger(StdOutSqlLogger)

        UserTable.select {
            UserTable.userName eq constrainedUsername
        }.firstNotNullOfOrNull {
            return@transaction verifyLongPassword(password, it[UserTable.password])
        }
        false
    }
}