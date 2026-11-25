package com.example.studentbudget.ui.statistics

import com.example.studentbudget.data.Expense
import com.example.studentbudget.data.ExpenseCategory
import java.time.YearMonth

internal data class MonthlyStatistics(
    val total: Long,
    val categories: List<Pair<ExpenseCategory, Long>>,
    val largestExpense: Expense?
)

internal fun List<Expense>.statisticsFor(month: YearMonth): MonthlyStatistics {
    val monthlyExpenses = filter { YearMonth.from(it.purchaseDate) == month }
    val categories = monthlyExpenses.groupBy { it.category }.map { (category, entries) ->
        category to entries.sumOf { it.amount.toLong() }
    }.sortedWith(compareByDescending<Pair<ExpenseCategory, Long>> { it.second }.thenBy { it.first })
    return MonthlyStatistics(
        total = categories.sumOf { it.second },
        categories = categories,
        largestExpense = monthlyExpenses.maxWithOrNull(compareBy<Expense> { it.amount }.thenBy { it.id })
    )
}
