package com.example.studentbudget.ui.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.studentbudget.ui.expenses.formatTenge
import com.example.studentbudget.viewmodel.MonthSummaries
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
internal fun StatisticsScreen(summaries: MonthSummaries) {
    val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)
    var showPrevious by rememberSaveable { mutableStateOf(false) }
    val month = if (showPrevious) summaries.month.minusMonths(1) else summaries.month
    val statistics = if (showPrevious) summaries.previous else summaries.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Statistics",
                modifier = Modifier.semantics { heading() },
                style = MaterialTheme.typography.headlineMedium
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = !showPrevious,
                    onClick = { showPrevious = false },
                    label = { Text("This month") }
                )
                FilterChip(
                    selected = showPrevious,
                    onClick = { showPrevious = true },
                    label = { Text("Last month") }
                )
            }
        }
        item {
            Text(month.format(monthFormatter))
            Text(formatTenge(statistics.total), style = MaterialTheme.typography.headlineSmall)
            Text("Total spent")
        }
        if (statistics.largestExpense == null) {
            item { Text("No expenses for this month.") }
        } else {
            item {
                Text("Largest expense", style = MaterialTheme.typography.titleMedium)
                val largest = statistics.largestExpense
                Text(largest.title)
                Text(largest.category.displayName, style = MaterialTheme.typography.bodyMedium)
                Text(formatTenge(largest.amount.toLong()))
            }
            item { Text("Spending by category", style = MaterialTheme.typography.titleMedium) }
            items(statistics.categories, key = { it.first.name }) { (category, amount) ->
                CategorySpending(category, amount, statistics.total)
            }
        }
    }
}
