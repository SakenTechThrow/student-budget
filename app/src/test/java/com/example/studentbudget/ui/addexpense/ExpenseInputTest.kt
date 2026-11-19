package com.example.studentbudget.ui.addexpense

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ExpenseInputTest {
    @Test
    fun acceptsWholeAmounts() {
        assertEquals(1500, parseExpenseAmount("1500"))
    }

    @Test
    fun rejectsFractionalAmounts() {
        listOf("1500.50", "1500,50", "0.01").forEach {
            assertNull(it, parseExpenseAmount(it))
        }
    }

    @Test
    fun trimsSurroundingSpaces() {
        assertEquals(1500, parseExpenseAmount(" 1500 "))
    }

    @Test
    fun rejectsZeroAndNegativeAmounts() {
        listOf("0", "0.00", "0,00", "-1", "-1500.50").forEach {
            assertNull(it, parseExpenseAmount(it))
        }
    }

    @Test
    fun rejectsEmptyAndMalformedAmounts() {
        listOf("", " ", "abc", "1,2.3", "1.234", ".50", "1.", "1 500").forEach {
            assertNull(it, parseExpenseAmount(it))
        }
    }

    @Test
    fun acceptsMaximumIntAndRejectsOverflow() {
        assertEquals(Int.MAX_VALUE, parseExpenseAmount("2147483647"))
        assertNull(parseExpenseAmount("2147483648"))
    }

    fun rejectsNonFiniteAndScientificValues() {
        listOf("NaN", "Infinity", "1e3", "9".repeat(400)).forEach {
            assertNull(it, parseExpenseAmount(it))
        }
    }
}
