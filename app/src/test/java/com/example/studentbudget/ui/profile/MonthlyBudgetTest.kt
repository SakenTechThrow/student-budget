package com.example.studentbudget.ui.profile

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MonthlyBudgetTest {
    @Test
    fun checksWarningAndLimitBoundaries() {
        assertEquals(BudgetStatus.DISABLED, MonthlyBudget(100, null).status)
        assertEquals(BudgetStatus.WITHIN_LIMIT, MonthlyBudget(1599, 2000).status)
        assertEquals(BudgetStatus.NEAR_LIMIT, MonthlyBudget(1600, 2000).status)
        assertEquals(BudgetStatus.NEAR_LIMIT, MonthlyBudget(1999, 2000).status)
        assertEquals(BudgetStatus.REACHED, MonthlyBudget(2000, 2000).status)
        val exceeded = MonthlyBudget(2100, 2000)
        assertEquals(BudgetStatus.EXCEEDED, exceeded.status)
        assertEquals(-100L, exceeded.balance)
        assertEquals(1f, exceeded.progress, 0f)
    }

    @Test
    fun acceptsOnlyPositiveWholeTenge() {
        assertEquals(50000L, " 50000 ".toMonthlyLimit())
        for (invalid in listOf("", "0", "-10", "2.5", "9223372036854775808")) {
            assertNull(invalid.toMonthlyLimit())
        }
        assertEquals("АК", profileInitials("  Айдын   Керембек "))
    }

    @Test
    fun warnsAtTheFirstWholeTengeAtOrAboveEightyPercent() {
        val threshold = Long.MAX_VALUE - Long.MAX_VALUE / 5
        assertEquals(BudgetStatus.WITHIN_LIMIT, MonthlyBudget(threshold - 1, Long.MAX_VALUE).status)
        assertEquals(BudgetStatus.NEAR_LIMIT, MonthlyBudget(threshold, Long.MAX_VALUE).status)
        assertEquals(BudgetStatus.REACHED, MonthlyBudget(Long.MAX_VALUE, Long.MAX_VALUE).status)
        assertEquals(Long.MAX_VALUE, Long.MAX_VALUE.toString().toMonthlyLimit())
    }
}
