package com.example.studentbudget.data

import com.example.studentbudget.data.local.ExpenseDao
import com.example.studentbudget.data.local.ExpenseEntity
import kotlinx.coroutines.flow.MutableStateFlow

internal class FakeExpenseDao : ExpenseDao {
    private val expenses = MutableStateFlow<List<ExpenseEntity>>(emptyList())
    private var nextId = 1

    override fun observeAll() = expenses

    override suspend fun findById(id: Int) = expenses.value.firstOrNull { it.id == id }

    override suspend fun insert(expense: ExpenseEntity): Long {
        val id = nextId++
        expenses.value = expenses.value + expense.copy(id = id)
        return id.toLong()
    }

    override suspend fun update(expense: ExpenseEntity): Int {
        val exists = expenses.value.any { it.id == expense.id }
        expenses.value = expenses.value.map { if (it.id == expense.id) expense else it }
        return if (exists) 1 else 0
    }

    override suspend fun deleteById(id: Int): Int {
        val remaining = expenses.value.filterNot { it.id == id }
        val removed = expenses.value.size - remaining.size
        expenses.value = remaining
        return removed
    }
}
