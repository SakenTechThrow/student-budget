package com.example.studentbudget.viewmodel

import androidx.lifecycle.ViewModel
import com.example.studentbudget.data.Expense
import com.example.studentbudget.data.ExpenseRepository
import kotlinx.coroutines.flow.StateFlow

/** Share a single instance between list, add and details screens. */
class BudgetViewModel(
    private val repository: ExpenseRepository = ExpenseRepository()
) : ViewModel() {
    val expenses: StateFlow<List<Expense>> = repository.expenses
}
