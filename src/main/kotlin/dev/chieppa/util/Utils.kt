package dev.chieppa.util

import dev.chieppa.config.config
import org.mindrot.jbcrypt.BCrypt
import java.security.MessageDigest

private val sha256Digest = MessageDigest.getInstance("SHA-256")

fun hashLongPassword(password: String): String =
    BCrypt.hashpw(hashPassword(password = password), BCrypt.gensalt(config.bcrypt.saltrounds))

fun verifyLongPassword(password: String, hashedPassword: String): Boolean {
    if (validPasswordLength(password)) {
        return BCrypt.checkpw(hashPassword(password = password), hashedPassword)
    }
    return false
}

fun hashPassword(pepper: String = config.users.pepper.value, password: String) =
    sha256Digest.digest("$pepper$password".toByteArray())
        .fold("") { str, it -> str + "%02x".format(it) }

fun validateUsername(username: String): Boolean {
    return username.matches(Regex(".+@.+[.].+"))
}

fun constrainUsername(username: String): String {
    return username.lowercase().trim()
}

fun validPasswordLength(password: String): Boolean {
    if (password.length < config.users.minPasswordLength) return false
    if (password.length > config.users.maxPasswordLength) return false
    return true
}

fun validatePassword(password: String): Boolean {
    if (!validPasswordLength(password)) return false
    if (!password.contains(Regex("[A-Z]+"))) return false
    if (!password.contains(Regex("[a-z]+"))) return false
    if (!password.contains(Regex("[0-9]+"))) return false
    if (!password.contains(Regex("\\W"))) return false
    return true
}