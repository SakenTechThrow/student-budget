package com.example.studentbudget.ui.expenses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun ExpenseCard(expense: ExpenseListItem) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(expense.title, style = MaterialTheme.typography.titleMedium)
                Text(expense.category, style = MaterialTheme.typography.bodyMedium)
            }
            Text(
                text = formatTenge(expense.amount.toLong()),
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}
