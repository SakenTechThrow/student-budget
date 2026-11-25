package com.example.studentbudget.ui.expenses

import com.example.studentbudget.data.Expense
import com.example.studentbudget.data.ExpenseCategory
import com.example.studentbudget.data.settings.ExpenseSort
import org.junit.Assert.assertEquals
import org.junit.Test

class ExpenseListLogicTest {
    private val sample = listOf(
        Expense(1, "Bus", 120, ExpenseCategory.TRANSPORT),
        Expense(2, "Lunch", 1500, ExpenseCategory.FOOD),
        Expense(3, "Coffee", 1500, ExpenseCategory.FOOD),
        Expense(4, "Book", 600, ExpenseCategory.EDUCATION)
    )

    @Test fun categorySummaryUsesAllExpenses() {
        assertEquals(3000L, categoryTotals(sample)[ExpenseCategory.FOOD])
        assertEquals(120L, categoryTotals(sample)[ExpenseCategory.TRANSPORT])
    }

    @Test fun newestUsesInsertionOrder() {
        assertEquals(listOf(4, 3, 2, 1), visibleExpenses(sample, null, ExpenseSort.NEWEST_FIRST).map { it.id })
    }

    @Test fun equalAmountShowsNewestFirst() {
        assertEquals(listOf(3, 2, 4, 1), visibleExpenses(sample, null, ExpenseSort.HIGHEST_AMOUNT_FIRST).map { it.id })
    }

    @Test fun filterDoesNotChangeCategoryTotals() {
        assertEquals(listOf(3, 2), visibleExpenses(sample, ExpenseCategory.FOOD, ExpenseSort.NEWEST_FIRST).map { it.id })
        assertEquals(4, sample.size)
        assertEquals(emptyList<Expense>(), visibleExpenses(emptyList(), null, ExpenseSort.NEWEST_FIRST))
    }
}
