package com.example.studentbudget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.studentbudget.ui.theme.StudentBudgetTheme
import androidx.compose.material3.Surface
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
                Surface {
                    ExpenseListScreen(sampleExpenses, onAddExpense = {}, onExpenseClick = {})
                }
            }
        }
    }
}
