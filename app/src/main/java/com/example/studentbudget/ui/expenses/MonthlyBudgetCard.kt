package com.example.studentbudget.ui.expenses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentbudget.ui.profile.BudgetStatus
import com.example.studentbudget.ui.profile.MonthlyBudget

@Composable
internal fun MonthlyBudgetCard(budget: MonthlyBudget) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Monthly budget", style = MaterialTheme.typography.titleMedium)
            Text("Spent this month: ${formatTenge(budget.spent)}")
            val limit = budget.limit
            val balance = budget.balance
            if (limit == null || balance == null) {
                Text("Set a monthly limit in Profile.")
            } else {
                Text("Limit: ${formatTenge(limit)}")
                LinearProgressIndicator({ budget.progress }, modifier = Modifier.fillMaxWidth())
                if (balance >= 0) Text("Remaining: ${formatTenge(balance)}")
                val warning = when (budget.status) {
                    BudgetStatus.NEAR_LIMIT -> "At least 80% of your monthly limit used."
                    BudgetStatus.REACHED -> "Monthly limit reached."
                    BudgetStatus.EXCEEDED -> "Over your monthly limit by ${formatTenge(-balance)}."
                    else -> null
                }
                if (warning != null) Text(warning, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
