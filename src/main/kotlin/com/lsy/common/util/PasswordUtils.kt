package com.lsy.common.util

import org.springframework.security.crypto.bcrypt.BCrypt

fun encrypt(password: String): String {
    return BCrypt.hashpw(password, BCrypt.gensalt())
}

fun matches(password: String, hashedPassword: String): Boolean {
    return BCrypt.checkpw(password, hashedPassword)
}