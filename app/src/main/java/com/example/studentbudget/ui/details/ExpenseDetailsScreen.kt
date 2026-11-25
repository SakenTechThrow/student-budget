package com.example.studentbudget.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.studentbudget.R
import com.example.studentbudget.data.Expense
import com.example.studentbudget.ui.expenses.formatTenge
import kotlinx.coroutines.launch

@Composable
internal fun ExpenseDetailsScreen(
    expense: Expense?,
    onBack: () -> Unit,
    onEdit: () -> Unit = {},
    onDelete: suspend () -> Boolean = { false }
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deleteError by remember { mutableStateOf(false) }
    var deleting by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().safeDrawingPadding()
            .verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (expense == null) {
            Text(stringResource(R.string.expense_not_found), style = MaterialTheme.typography.headlineMedium)
        } else {
            Text(expense.title, style = MaterialTheme.typography.headlineMedium)
            Text(expense.category.displayName, style = MaterialTheme.typography.bodyLarge)
            Text(formatTenge(expense.amount.toLong()), style = MaterialTheme.typography.titleLarge)
            Text("Purchased on: ${expense.purchaseDate}")
            OutlinedButton(onClick = onEdit, enabled = !deleting, modifier = Modifier.fillMaxWidth()) {
                Text("Edit")
            }
            Button(onClick = { showDeleteDialog = true }, enabled = !deleting, modifier = Modifier.fillMaxWidth()) {
                Text("Delete expense")
            }
            if (deleteError) {
                Text("Could not delete expense. Try again.", color = MaterialTheme.colorScheme.error)
            }
        }
        OutlinedButton(onClick = onBack, enabled = !deleting) {
            Text(stringResource(R.string.back))
        }
    }

    if (showDeleteDialog && expense != null) {
        AlertDialog(
            onDismissRequest = { if (!deleting) showDeleteDialog = false },
            title = { Text("Delete expense?") },
            text = { Text("This will permanently delete ${expense.title}.") },
            confirmButton = {
                TextButton(enabled = !deleting, onClick = {
                    deleting = true
                    scope.launch {
                        try {
                            if (onDelete()) {
                                showDeleteDialog = false
                                onBack()
                            } else {
                                deleteError = true
                                showDeleteDialog = false
                            }
                        } finally {
                            deleting = false
                        }
                    }
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(enabled = !deleting, onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
