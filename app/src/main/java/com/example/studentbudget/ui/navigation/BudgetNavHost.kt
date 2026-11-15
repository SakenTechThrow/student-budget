package com.example.studentbudget.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studentbudget.ui.expenses.ExpenseListItem
import com.example.studentbudget.ui.expenses.ExpenseListScreen
import com.example.studentbudget.viewmodel.BudgetViewModel

@Composable
internal fun BudgetNavHost(viewModel: BudgetViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = BudgetRoutes.EXPENSES) {
        composable(BudgetRoutes.EXPENSES) {
            val expenses by viewModel.expenses.collectAsStateWithLifecycle()
            ExpenseListScreen(
                expenses = expenses.map { expense ->
                    ExpenseListItem(expense.id, expense.title, expense.amount, expense.category.displayName)
                },
                onAddExpense = {},
                onExpenseClick = {}
            )
        }
    }
}
