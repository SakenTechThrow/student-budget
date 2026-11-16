package com.example.studentbudget.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** In-memory storage; changes are reset when the app process restarts. */
class ExpenseRepository {
    private val _expenses = MutableStateFlow(
        listOf(
            Expense(1, "Bus", 120, ExpenseCategory.TRANSPORT),
            Expense(2, "Coffee", 800, ExpenseCategory.FOOD)
        )
    )

    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()
}
