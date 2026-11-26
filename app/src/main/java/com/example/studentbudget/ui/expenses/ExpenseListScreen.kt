package com.example.studentbudget.ui.expenses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import com.example.studentbudget.R

@Composable
fun ExpenseListScreen(
    expenses: List<ExpenseListItem>,
    onAddExpense: () -> Unit,
    onExpenseClick: (Int) -> Unit,
    total: Long = expenses.sumOf { it.amount.toLong() },
    monthlyBudget: (@Composable () -> Unit)? = null
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
            if (monthlyBudget != null) item { monthlyBudget() }
            if (expenses.isEmpty()) {
                item {
                    Text(stringResource(R.string.no_expenses), style = MaterialTheme.typography.bodyLarge)
                }
            }
            item {
                Button(onClick = onAddExpense) {
                    Text(stringResource(R.string.add_expense))
                }
            }
            items(expenses, key = { expense -> expense.id }) { expense ->
                ExpenseCard(expense, onClick = { onExpenseClick(expense.id) })
            }
        }
    }
}
