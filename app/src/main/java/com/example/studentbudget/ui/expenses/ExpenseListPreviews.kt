package com.example.studentbudget.ui.expenses

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.studentbudget.ui.theme.StudentBudgetTheme

@Preview(showBackground = true)
@Composable
private fun ExpenseListPreview() {
    val expenses = listOf(
        ExpenseListItem(1, "Bus", 120, "Transport"),
        ExpenseListItem(2, "Coffee", 800, "Food")
    )
    StudentBudgetTheme {
        ExpenseListScreen(expenses, 920, onExpenseClick = {}, onAddExpense = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyExpenseListPreview() {
    StudentBudgetTheme {
        ExpenseListScreen(emptyList(), 0, onExpenseClick = {}, onAddExpense = {})
    }
}
