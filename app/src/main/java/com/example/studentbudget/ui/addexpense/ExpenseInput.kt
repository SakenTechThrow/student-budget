package com.example.studentbudget.ui.addexpense

internal fun parseExpenseAmount(value: String): Double? {
    val normalized = value.trim().replace(',', '.')
    if (!normalized.matches(Regex("[0-9]+(\\.[0-9]{1,2})?"))) return null
    return normalized.toDoubleOrNull()?.takeIf { it.isFinite() && it > 0 }
}
