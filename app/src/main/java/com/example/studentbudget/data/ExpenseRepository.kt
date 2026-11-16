package com.example.studentbudget.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** In-memory storage; changes are reset when the app process restarts. */
class ExpenseRepository {
    private val _expenses = MutableStateFlow(
        listOf(
            Expense(1, "Bus", 120, ExpenseCategory.TRANSPORT),
            Expense(2, "Coffee", 800, ExpenseCategory.FOOD)
        )
    )

    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    @Synchronized
    fun addExpense(title: String, amount: Int, category: ExpenseCategory): Expense {
        val cleanTitle = title.trim()
        require(cleanTitle.isNotEmpty()) { "Expense title cannot be empty" }
        require(amount > 0) { "Expense amount must be positive" }
        val highestId = _expenses.value.maxOfOrNull { it.id } ?: 0
        check(highestId < Int.MAX_VALUE) { "Expense ID limit reached" }
        val newExpense = Expense(highestId + 1, cleanTitle, amount, category)
        _expenses.update { it + newExpense }
        return newExpense
    }
}
