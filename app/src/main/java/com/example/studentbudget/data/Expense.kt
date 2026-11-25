package com.example.studentbudget.data

import java.time.LocalDate

/** Amounts are stored as whole tenge; IDs are compatible with the expense-list UI. */
data class Expense(
    val id: Int,
    val title: String,
    val amount: Int,
    val category: ExpenseCategory,
    val purchaseDate: LocalDate = LocalDate.now()
)
