package com.example.studentbudget.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.studentbudget.R
import com.example.studentbudget.data.Expense
import com.example.studentbudget.ui.expenses.formatTenge

@Composable
internal fun ExpenseDetailsScreen(expense: Expense?, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (expense == null) {
            Text(stringResource(R.string.expense_not_found), style = MaterialTheme.typography.headlineMedium)
        } else {
            Text(expense.title, style = MaterialTheme.typography.headlineMedium)
            Text(expense.category.displayName, style = MaterialTheme.typography.bodyLarge)
            Text(formatTenge(expense.amount.toLong()), style = MaterialTheme.typography.titleLarge)
        }
        OutlinedButton(onClick = onBack) {
            Text(stringResource(R.string.back))
        }
    }
}
