package com.example.studentbudget.ui.expenses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.studentbudget.ui.theme.StudentBudgetTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import com.example.studentbudget.R

@Composable
fun ExpenseListScreen(expenses: List<ExpenseListItem>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().safeDrawingPadding(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(stringResource(R.string.app_name), style = MaterialTheme.typography.headlineMedium)
        }
        items(expenses, key = { expense -> expense.id }) { expense ->
            Text(expense.title, style = MaterialTheme.typography.titleMedium)
            Text(expense.category + " · " + expense.amount + " ₸", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
