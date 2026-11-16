package com.example.studentbudget.data

import kotlinx.coroutines.flow.MutableStateFlow

/** In-memory storage; changes are reset when the app process restarts. */
class ExpenseRepository {
    private val _expenses = MutableStateFlow(
        listOf(
            Expense(1, "Bus", 120, ExpenseCategory.TRANSPORT),
            Expense(2, "Coffee", 800, ExpenseCategory.FOOD)
        )
    )
}
