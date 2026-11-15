package com.example.studentbudget.ui.expenses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.studentbudget.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    expenses: List<ExpenseListItem>,
    total: Long,
    onExpenseClick: (Int) -> Unit,
    onAddExpense: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) },
        floatingActionButton = {
            Button(onClick = onAddExpense) {
                Text(stringResource(R.string.add_expense))
            }
        }
    ) { insets ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(insets),
            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 88.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { TotalSpentCard(total) }
            if (expenses.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.no_expenses),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            items(expenses, key = { it.id }) { expense ->
                ExpenseCard(expense, onClick = { onExpenseClick(expense.id) })
            }
        }
    }
}
