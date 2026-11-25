package com.example.studentbudget.data

import com.example.studentbudget.data.local.ExpenseDao
import com.example.studentbudget.data.local.toEntity
import com.example.studentbudget.data.local.toExpense
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomExpenseRepository(private val dao: ExpenseDao) {
    val expenses: Flow<List<Expense>> = dao.observeAll().map { rows ->
        rows.map { it.toExpense() }
    }

    suspend fun findById(id: Int): Expense? = dao.findById(id)?.toExpense()

    suspend fun addExpense(
        title: String,
        amount: Int,
        category: ExpenseCategory,
        purchaseDate: LocalDate = LocalDate.now()
    ): Expense {
        val expense = Expense(0, validate(title, amount), amount, category, purchaseDate)
        val id = dao.insert(expense.toEntity())
        return expense.copy(id = id.toInt())
    }

    suspend fun updateExpense(expense: Expense): Boolean {
        require(expense.id > 0) { "Expense ID must be positive" }
        val validated = expense.copy(title = validate(expense.title, expense.amount))
        return dao.update(validated.toEntity()) == 1
    }

    suspend fun deleteExpense(id: Int): Boolean = dao.deleteById(id) == 1

    private fun validate(title: String, amount: Int): String {
        val cleanTitle = title.trim()
        require(cleanTitle.isNotEmpty()) { "Expense title cannot be empty" }
        require(amount > 0) { "Expense amount must be positive" }
        return cleanTitle
    }
}
