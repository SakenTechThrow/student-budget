package com.example.studentbudget.ui.expenses

import com.example.studentbudget.data.Expense
import com.example.studentbudget.data.ExpenseCategory
import com.example.studentbudget.data.settings.ExpenseSort

/** Derive both the category summary and filtered list from the same Room-backed data. */
internal fun categoryTotals(expenses: List<Expense>): Map<ExpenseCategory, Long> =
    expenses.groupBy { it.category }.mapValues { (_, entries) ->
        entries.sumOf { it.amount.toLong() }
    }

internal fun visibleExpenses(
    expenses: List<Expense>,
    category: ExpenseCategory?,
    sort: ExpenseSort
): List<Expense> {
    val filtered = if (category == null) expenses else expenses.filter { it.category == category }
    return when (sort) {
        ExpenseSort.NEWEST_FIRST -> filtered.sortedByDescending { it.id }
        ExpenseSort.HIGHEST_AMOUNT_FIRST ->
            filtered.sortedWith(compareByDescending<Expense> { it.amount }.thenByDescending { it.id })
    }
}
