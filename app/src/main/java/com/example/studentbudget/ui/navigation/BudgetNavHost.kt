package com.example.studentbudget.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studentbudget.data.ExpenseCategory
import com.example.studentbudget.ui.addexpense.AddExpenseScreen
import com.example.studentbudget.ui.expenses.ExpenseListItem
import com.example.studentbudget.ui.expenses.ExpenseListScreen
import com.example.studentbudget.viewmodel.BudgetViewModel

@Composable
internal fun BudgetNavHost(viewModel: BudgetViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = BudgetRoutes.EXPENSES) {
        composable(BudgetRoutes.EXPENSES) {
            val expenses by viewModel.expenses.collectAsStateWithLifecycle()
            val total by viewModel.totalAmount.collectAsStateWithLifecycle()
            ExpenseListScreen(
                expenses = expenses.map { expense ->
                    ExpenseListItem(expense.id, expense.title, expense.amount, expense.category.displayName)
                },
                onAddExpense = {
                    navController.navigate(BudgetRoutes.ADD_EXPENSE) { launchSingleTop = true }
                },
                onExpenseClick = {},
                total = total
            )
        }
        composable(BudgetRoutes.ADD_EXPENSE) {
            val categories = ExpenseCategory.entries.associateBy(ExpenseCategory::displayName)
            AddExpenseScreen(
                categories = categories.keys.toList(),
                onSave = { title, amount, categoryName ->
                    val category = categories[categoryName]
                    category != null &&
                        viewModel.addExpense(title, amount.toString(), category) == null
                },
                onBack = { navController.popBackStack(BudgetRoutes.EXPENSES, inclusive = false) }
            )
        }
    }
}
