package com.example.studentbudget.data

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class ExpenseValidationTest {
    private val repository = ExpenseRepository(FakeExpenseDao())

    @Test fun blankNameIsRejected() = runBlocking {
        assertThrows(IllegalArgumentException::class.java) {
            runBlocking { repository.addExpense("  ", 1500, ExpenseCategory.FOOD) }
        }
        assertTrue(repository.expenses.first().isEmpty())
    }

    @Test fun zeroAndNegativeAmountsAreRejected() = runBlocking {
        for (amount in listOf(0, -1)) {
            assertThrows(IllegalArgumentException::class.java) {
                runBlocking { repository.addExpense("Lunch", amount, ExpenseCategory.FOOD) }
            }
        }
        assertTrue(repository.expenses.first().isEmpty())
    }

    @Test fun titleIsTrimmedAndMissingIdReturnsNull() = runBlocking {
        val lunch = repository.addExpense("  Lunch  ", 1500, ExpenseCategory.FOOD)
        assertEquals("Lunch", lunch.title)
        assertNull(repository.findById(-123))
    }
}
