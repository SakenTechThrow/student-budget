package com.example.studentbudget.ui.profile

internal data class MonthlyBudget(val spent: Long, val limit: Long?) {
    val balance: Long? get() = limit?.minus(spent)
    val progress: Float get() = limit?.let { (spent.toDouble() / it).coerceIn(0.0, 1.0).toFloat() } ?: 0f

    val status: BudgetStatus
        get() = when {
            limit == null -> BudgetStatus.DISABLED
            spent > limit -> BudgetStatus.EXCEEDED
            spent == limit -> BudgetStatus.REACHED
            spent >= limit - limit / 5 -> BudgetStatus.NEAR_LIMIT
            else -> BudgetStatus.WITHIN_LIMIT
        }
}

internal enum class BudgetStatus { DISABLED, WITHIN_LIMIT, NEAR_LIMIT, REACHED, EXCEEDED }
