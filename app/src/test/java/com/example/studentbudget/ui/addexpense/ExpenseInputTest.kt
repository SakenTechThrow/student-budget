package com.example.studentbudget.ui.addexpense

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ExpenseInputTest {
    @Test
    fun acceptsWholeAmounts() {
        assertEquals(1500.0, parseExpenseAmount("1500")!!, 0.0)
    }

    @Test
    fun acceptsDecimalPointAndComma() {
        assertEquals(1500.50, parseExpenseAmount("1500.50")!!, 0.0)
        assertEquals(1500.50, parseExpenseAmount("1500,50")!!, 0.0)
        assertEquals(0.01, parseExpenseAmount("0.01")!!, 0.0)
    }

    @Test
    fun trimsSurroundingSpaces() {
        assertEquals(1500.0, parseExpenseAmount(" 1500 ")!!, 0.0)
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
    fun rejectsNonFiniteAndScientificValues() {
        listOf("NaN", "Infinity", "1e3", "9".repeat(400)).forEach {
            assertNull(it, parseExpenseAmount(it))
        }
    }
}
