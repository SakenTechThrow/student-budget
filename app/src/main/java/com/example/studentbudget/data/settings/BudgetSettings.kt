package com.example.studentbudget.data.settings

enum class BudgetTheme { SYSTEM, LIGHT, DARK }

enum class ExpenseSort { NEWEST_FIRST, HIGHEST_AMOUNT_FIRST }

data class BudgetSettings(
    val profileName: String = "",
    val theme: BudgetTheme = BudgetTheme.SYSTEM,
    val expenseSort: ExpenseSort = ExpenseSort.NEWEST_FIRST,
    val monthlyLimit: Long? = null
)
