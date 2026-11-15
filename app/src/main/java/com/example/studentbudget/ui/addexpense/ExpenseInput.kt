package com.example.studentbudget.ui.addexpense

internal fun parseExpenseAmount(value: String): Int? =
    value.trim().toIntOrNull()?.takeIf { it > 0 }
