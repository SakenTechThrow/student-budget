package com.example.studentbudget.data

import java.time.LocalDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExpenseRepositoryTest {
    private val repository = ExpenseRepository(FakeExpenseDao())

    @Test fun newRepositoryHasNoDemoExpenses() = runBlocking {
        assertTrue(repository.expenses.first().isEmpty())
    }

    @Test fun supportsCrudAndPreservesPurchaseDate() = runBlocking {
        val date = LocalDate.of(2026, 10, 31)
        val lunch = repository.addExpense("Lunch", 1500, ExpenseCategory.FOOD, date)
        assertEquals(listOf(lunch), repository.expenses.first())
        assertEquals(date, repository.findById(lunch.id)?.purchaseDate)

        val edited = lunch.copy(title = " Late lunch ", amount = 1800, purchaseDate = date.plusDays(1))
        assertTrue(repository.updateExpense(edited))
        assertEquals(listOf(edited.copy(title = "Late lunch")), repository.expenses.first())

        assertTrue(repository.deleteExpense(lunch.id))
        assertFalse(repository.deleteExpense(lunch.id))
        assertTrue(repository.expenses.first().isEmpty())
    }

    @Test fun addingAfterDeletionKeepsIdsUnique() = runBlocking {
        val first = repository.addExpense("Lunch", 1500, ExpenseCategory.FOOD)
        repository.deleteExpense(first.id)
        val second = repository.addExpense("Snack", 300, ExpenseCategory.FOOD)
        assertTrue(second.id > first.id)
    }
}
