package com.example.studentbudget.ui.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentbudget.data.ExpenseCategory
import com.example.studentbudget.ui.expenses.formatTenge

@Composable
internal fun CategorySpending(category: ExpenseCategory, amount: Long, total: Long) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(category.displayName)
            Text(formatTenge(amount), style = MaterialTheme.typography.labelLarge)
        }
        LinearProgressIndicator(
            progress = { (amount.toDouble() / total).toFloat() },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
