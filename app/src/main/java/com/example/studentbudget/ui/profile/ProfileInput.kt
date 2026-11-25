package com.example.studentbudget.ui.profile

internal fun profileInitials(name: String): String =
    name.trim().split(Regex("\\s+"))
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { word -> word.substring(0, word.offsetByCodePoints(0, 1)).uppercase() }

internal fun String.toMonthlyLimit(): Long? =
    trim().takeIf { it.all(Char::isDigit) }?.toLongOrNull()?.takeIf { it > 0 }
