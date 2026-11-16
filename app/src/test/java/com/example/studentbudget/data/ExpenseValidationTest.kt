package com.example.studentbudget.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ExpenseValidationTest {
    @Test fun blankNameIsRejected() {
        val repository = ExpenseRepository()
        assertThrows(IllegalArgumentException::class.java) {
            repository.addExpense("  ", 1500, ExpenseCategory.FOOD)
        }
        assertEquals(2, repository.expenses.value.size)
    }

    @Test fun zeroAndNegativeAmountsAreRejected() {
        val repository = ExpenseRepository()
        for (amount in listOf(0, -1)) {
            assertThrows(IllegalArgumentException::class.java) {
                repository.addExpense("Lunch", amount, ExpenseCategory.FOOD)
            }
        }
        assertEquals(2, repository.expenses.value.size)
    }

    @Test fun titleIsTrimmedAndMissingIdReturnsNull() {
        val repository = ExpenseRepository()
        val lunch = repository.addExpense("  Lunch  ", 1500, ExpenseCategory.FOOD)
        assertEquals("Lunch", lunch.title)
        assertEquals(null, repository.findById(-123))
    }
}
