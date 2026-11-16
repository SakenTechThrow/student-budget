package com.example.studentbudget.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ExpenseRepositoryTest {
    @Test fun initialExpensesTotal920() {
        val repository = ExpenseRepository()
        assertEquals(2, repository.expenses.value.size)
        assertEquals(920L, repository.expenses.value.sumOf { it.amount.toLong() })
    }

    @Test fun addingLunchMakesTotal2420() {
        val repository = ExpenseRepository()
        val lunch = repository.addExpense("Lunch", 1500, ExpenseCategory.FOOD)
        assertEquals(2420L, repository.expenses.value.sumOf { it.amount.toLong() })
        assertEquals(lunch, repository.findById(lunch.id))
        assertNotNull(repository.findById(1))
    }

    @Test fun idsRemainUnique() {
        val repository = ExpenseRepository()
        val first = repository.addExpense("Lunch", 1500, ExpenseCategory.FOOD)
        val second = repository.addExpense("Snack", 300, ExpenseCategory.FOOD)
        assertEquals(first.id + 1, second.id)
    }
}
