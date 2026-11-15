package com.example.studentbudget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.example.studentbudget.ui.expenses.ExpenseListItem
import com.example.studentbudget.ui.expenses.ExpenseListScreen
import com.example.studentbudget.ui.theme.StudentBudgetTheme
import kotlinx.coroutines.launch

private val demoExpenses = listOf(
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
                    expenses = demoExpenses,
                    total = demoExpenses.sumOf { it.amount.toLong() },
                    onExpenseClick = { id ->
                        val expense = demoExpenses.first { it.id == id }
                        scope.launch { snackbar.showSnackbar(expense.title) }
                    },
                    onAddExpense = { scope.launch { snackbar.showSnackbar(addMessage) } },
                    snackbarHost = { SnackbarHost(snackbar) }
                )
            }
        }
    }
}
