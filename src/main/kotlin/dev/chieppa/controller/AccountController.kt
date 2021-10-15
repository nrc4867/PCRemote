package dev.chieppa.controller

import dev.chieppa.config.plugins.UserSession
import dev.chieppa.model.table.SessionIDTable
import dev.chieppa.model.table.UserTable
import dev.chieppa.util.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.*

fun createSession(username: String): UserSession {
    val userSession = UserSession(username, UUID.randomUUID().toString())
    transaction {
        SessionIDTable.insert {
            it[userName] = userSession.username
            it[sessionId] = userSession.sessionId
            it[createdAt] = LocalDateTime.now()
        }
    }
    return userSession
}

fun deleteSession(session: UserSession) {
    transaction {
        SessionIDTable.deleteWhere { SessionIDTable.userName eq session.username and (SessionIDTable.sessionId eq session.sessionId) }
    }
}

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

fun userExits(username: String): Boolean {
    return transaction {
        UserTable.select { UserTable.userName eq username }.firstNotNullOfOrNull { return@transaction true } ?: false
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