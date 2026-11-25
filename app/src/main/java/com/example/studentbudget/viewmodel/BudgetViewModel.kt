package com.example.studentbudget.viewmodel

import android.database.sqlite.SQLiteException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentbudget.data.Expense
import com.example.studentbudget.data.ExpenseCategory
import com.example.studentbudget.data.ExpenseRepository
import com.example.studentbudget.data.settings.SettingsRepository
import com.example.studentbudget.data.settings.BudgetSettings
import com.example.studentbudget.data.settings.ExpenseSort
import java.time.LocalDate
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/** Shared between list, add and details screens. */
class BudgetViewModel(
    private val repository: ExpenseRepository,
    val settingsRepository: SettingsRepository
) : ViewModel() {
    val settings: StateFlow<BudgetSettings> = settingsRepository.settings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = BudgetSettings()
    )

    suspend fun setExpenseSort(sort: ExpenseSort) = settingsRepository.setExpenseSort(sort)

    val expenses: StateFlow<List<Expense>> = repository.expenses.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    val totalAmount: StateFlow<Long> = expenses.map { entries ->
        entries.sumOf { it.amount.toLong() }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0L
    )

    suspend fun updateExpense(expense: Expense): String? {
        if (expense.title.isBlank()) return "Enter an expense title"
        if (expense.amount <= 0) return "Amount must be greater than zero"
        return try {
            if (repository.updateExpense(expense)) null else "Expense no longer exists"
        } catch (_: SQLiteException) {
            "Could not update the expense. Try again."
        }
    }

    /** Returns null on success, otherwise an error message for the form. */
    suspend fun addExpense(
        title: String,
        amountText: String,
        category: ExpenseCategory,
        purchaseDate: LocalDate = LocalDate.now()
    ): String? {
        if (title.isBlank()) return "Enter an expense title"
        val amount = amountText.trim().toIntOrNull()
            ?: return "Enter a valid amount in tenge"
        if (amount <= 0) return "Amount must be greater than zero"
        return try {
            repository.addExpense(title, amount, category, purchaseDate)
            null
        } catch (_: SQLiteException) {
            "Could not save the expense. Try again."
        }
    }
}
