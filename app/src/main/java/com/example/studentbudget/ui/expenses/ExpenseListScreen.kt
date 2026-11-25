package com.example.studentbudget.ui.expenses

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.studentbudget.data.settings.ExpenseSort
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.studentbudget.R

@Composable
fun ExpenseListScreen(
    expenses: List<ExpenseListItem>,
    onAddExpense: () -> Unit,
    onExpenseClick: (Int) -> Unit,
    total: Long = expenses.sumOf { it.amount.toLong() },
    categorySummary: Map<String, Long> = emptyMap(),
    selectedCategory: String? = null,
    onCategorySelected: (String?) -> Unit = {},
    sort: ExpenseSort = ExpenseSort.NEWEST_FIRST,
    onSortChange: (ExpenseSort) -> Unit = {}
) {
    Scaffold { insets ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(insets),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    stringResource(R.string.app_name),
                    modifier = Modifier.semantics { heading() },
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            item { TotalSpentCard(total) }
            if (categorySummary.isNotEmpty()) {
                item {
                    Text("Categories", style = MaterialTheme.typography.titleMedium)
                }
                item {
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { onCategorySelected(null) },
                            label = { Text("All") }
                        )
                        categorySummary.forEach { (category, amount) ->
                            FilterChip(
                                selected = selectedCategory == category,
                                onClick = { onCategorySelected(category) },
                                label = { Text("$category · ${formatTenge(amount)}") }
                            )
                        }
                    }
                }
            }
            item {
                var sortMenuOpen by remember { mutableStateOf(false) }
                OutlinedButton(onClick = { sortMenuOpen = true }) {
                    Text(if (sort == ExpenseSort.NEWEST_FIRST) "Newest first" else "Highest amount first")
                }
                DropdownMenu(expanded = sortMenuOpen, onDismissRequest = { sortMenuOpen = false }) {
                    ExpenseSort.entries.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(if (option == ExpenseSort.NEWEST_FIRST) "Newest first"
                                     else "Highest amount first")
                            },
                            onClick = {
                                sortMenuOpen = false
                                onSortChange(option)
                            }
                        )
                    }
                }
            }
            item {
                Button(onClick = onAddExpense) {
                    Text(stringResource(R.string.add_expense))
                }
            }
            if (expenses.isEmpty()) {
                item {
                    Text(
                        if (selectedCategory == null) stringResource(R.string.no_expenses)
                        else "No expenses in this category",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            items(expenses, key = { expense -> expense.id }) { expense ->
                ExpenseCard(expense, onClick = { onExpenseClick(expense.id) })
            }
        }
    }
}
