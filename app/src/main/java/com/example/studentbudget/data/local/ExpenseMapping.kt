package com.example.studentbudget.data.local

import com.example.studentbudget.data.Expense
import com.example.studentbudget.data.ExpenseCategory
import java.time.LocalDate

internal fun ExpenseEntity.toExpense() = Expense(
    id = id,
    title = title,
    amount = amount,
    category = ExpenseCategory.valueOf(category),
    purchaseDate = LocalDate.ofEpochDay(purchaseEpochDay)
)

internal fun Expense.toEntity() = ExpenseEntity(
    id = id,
    title = title,
    amount = amount,
    category = category.name,
    purchaseEpochDay = purchaseDate.toEpochDay()
)
