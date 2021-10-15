package dev.chieppa.config.model.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.datetime

object SessionIDTable: Table() {
    val userName = varchar("username", length = 40)
    val sessionId = char("session_id", length = 36)
    val createdAt = datetime("created_at")

    override val primaryKey: Table.PrimaryKey = PrimaryKey(userName, sessionId)
}