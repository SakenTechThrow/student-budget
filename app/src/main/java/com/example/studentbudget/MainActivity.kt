package com.example.studentbudget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.studentbudget.ui.theme.StudentBudgetTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch
import com.example.studentbudget.ui.expenses.ExpenseListItem
import com.example.studentbudget.ui.expenses.ExpenseListScreen

private val sampleExpenses = listOf(
    ExpenseListItem(1, "Bus", 120, "Transport"),
    ExpenseListItem(2, "Coffee", 800, "Food")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentBudgetTheme {
                val snackbar = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                val addMessage = stringResource(R.string.demo_add_message)
                ExpenseListScreen(
                    expenses = sampleExpenses,
                    onAddExpense = { scope.launch { snackbar.showSnackbar(addMessage) } },
                    onExpenseClick = { id ->
                        val title = sampleExpenses.first { it.id == id }.title
                        scope.launch { snackbar.showSnackbar(title) }
                    },
                    snackbarHost = { SnackbarHost(snackbar) }
                )
            }
        }
    }
}
