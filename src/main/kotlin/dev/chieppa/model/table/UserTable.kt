package dev.chieppa.model.table

import org.jetbrains.exposed.sql.Table

object UserTable : Table() {
    val userName = varchar("username", length = 40)
    val password = char("password", length = 60)

    override val primaryKey: PrimaryKey = PrimaryKey(userName)
}