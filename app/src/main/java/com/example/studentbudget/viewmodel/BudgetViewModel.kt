package com.example.studentbudget.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentbudget.data.Expense
import com.example.studentbudget.data.ExpenseRepository
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
}
