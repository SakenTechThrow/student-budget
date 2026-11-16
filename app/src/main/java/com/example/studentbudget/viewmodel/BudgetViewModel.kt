package com.example.studentbudget.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentbudget.data.Expense
import com.example.studentbudget.data.ExpenseRepository
import com.example.studentbudget.data.ExpenseCategory
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/** Share a single instance between list, add and details screens. */
class BudgetViewModel(
    private val repository: ExpenseRepository = ExpenseRepository()
) : ViewModel() {
    val expenses: StateFlow<List<Expense>> = repository.expenses

    val totalAmount: StateFlow<Long> = expenses.map { entries ->
        entries.sumOf { it.amount.toLong() }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = expenses.value.sumOf { it.amount.toLong() }
    )

    fun findExpenseById(id: Int): Expense? = repository.findById(id)

    /** Returns null on success, otherwise an error message for the form. */
    fun addExpense(title: String, amountText: String, category: ExpenseCategory): String? {
        if (title.isBlank()) return "Enter an expense title"
        val amount = amountText.trim().toIntOrNull()
            ?: return "Enter a valid amount in tenge"
        if (amount <= 0) return "Amount must be greater than zero"
        repository.addExpense(title, amount, category)
        return null
    }
}
