package com.example.studentbudget.ui.statistics

import com.example.studentbudget.data.Expense
import com.example.studentbudget.data.ExpenseCategory
import java.time.YearMonth
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MonthlyStatisticsTest {
    @Test
    fun movingAnExpenseBetweenMonthsUpdatesBothSummaries() {
        val month = YearMonth.of(2026, 1)
        val previous = month.minusMonths(1)
        val lunch = Expense(1, "Lunch", 1500, ExpenseCategory.FOOD, month.atDay(1))
        val bus = Expense(2, "Bus", 120, ExpenseCategory.TRANSPORT, month.atDay(3))
        val before = listOf(lunch, bus).statisticsFor(month)
        assertEquals(1620L, before.total)
        assertEquals(listOf(ExpenseCategory.FOOD to 1500L, ExpenseCategory.TRANSPORT to 120L), before.categories)
        assertEquals(lunch, before.largestExpense)

        val moved = lunch.copy(amount = 1800, purchaseDate = previous.atEndOfMonth())
        val after = listOf(moved, bus)
        assertEquals(120L, after.statisticsFor(month).total)
        assertEquals(1800L, after.statisticsFor(previous).total)
        assertEquals(moved, after.statisticsFor(previous).largestExpense)

        val empty = emptyList<Expense>().statisticsFor(month)
        assertEquals(0L, empty.total)
        assertNull(empty.largestExpense)
    }

    @Test
    fun totalsBeyondIntRangeAndSelectsLargestByIdWhenAmountsMatch() {
        val month = YearMonth.of(2026, 11)
        val first = Expense(1, "Laptop", Int.MAX_VALUE, ExpenseCategory.EDUCATION, month.atDay(1))
        val second = first.copy(id = 2, title = "Equipment")
        val old = first.copy(id = 3, purchaseDate = month.minusYears(1).atDay(1))

        val statistics = listOf(second, old, first).statisticsFor(month)

        assertEquals(4_294_967_294L, statistics.total)
        assertEquals(listOf(ExpenseCategory.EDUCATION to 4_294_967_294L), statistics.categories)
        assertEquals(second, statistics.largestExpense)
    }
}
