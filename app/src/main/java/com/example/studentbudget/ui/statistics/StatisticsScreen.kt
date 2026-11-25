package com.example.studentbudget.ui.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
            Text(month.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)))
            Text(formatTenge(statistics.total), style = MaterialTheme.typography.headlineSmall)
            Text("Total spent")
        }
        if (statistics.largestExpense == null) {
            item { Text("No expenses for this month.") }
        } else {
            item {
                Text("Largest expense", style = MaterialTheme.typography.titleMedium)
                Text("${statistics.largestExpense.title} · ${formatTenge(statistics.largestExpense.amount.toLong())}")
            }
        }
    }
}
