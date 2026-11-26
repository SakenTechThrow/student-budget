package com.example.studentbudget.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.studentbudget.data.ExpenseCategory
import com.example.studentbudget.ui.addexpense.AddExpenseScreen
import com.example.studentbudget.ui.details.ExpenseDetailsScreen
import com.example.studentbudget.ui.expenses.ExpenseListItem
import com.example.studentbudget.ui.expenses.ExpenseListScreen
import com.example.studentbudget.ui.expenses.MonthlyBudgetCard
import com.example.studentbudget.ui.profile.MonthlyBudget
import com.example.studentbudget.ui.profile.ProfileScreen
import com.example.studentbudget.ui.statistics.StatisticsScreen
import com.example.studentbudget.viewmodel.BudgetViewModel

@Composable
internal fun BudgetNavHost(viewModel: BudgetViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = BudgetRoutes.EXPENSES) {
        composable(BudgetRoutes.EXPENSES) {
            val expenses by viewModel.expenses.collectAsStateWithLifecycle()
            val total by viewModel.totalAmount.collectAsStateWithLifecycle()
            val summaries by viewModel.monthlyStatistics.collectAsStateWithLifecycle()
            val settings by viewModel.settings.collectAsStateWithLifecycle()
            ExpenseListScreen(
                expenses = expenses.map { expense ->
                    ExpenseListItem(expense.id, expense.title, expense.amount, expense.category.displayName)
                },
                onAddExpense = {
                    navController.navigate(BudgetRoutes.ADD_EXPENSE) { launchSingleTop = true }
                },
                onExpenseClick = { expenseId ->
                    navController.navigate(BudgetRoutes.expenseDetails(expenseId)) {
                        launchSingleTop = true
                    }
                },
                total = total,
                monthlyBudget = { MonthlyBudgetCard(MonthlyBudget(summaries.current.total, settings.monthlyLimit)) }
            )
        }
        composable(BudgetRoutes.STATISTICS) {
            val summaries by viewModel.monthlyStatistics.collectAsStateWithLifecycle()
            StatisticsScreen(summaries)
        }
        composable(BudgetRoutes.PROFILE) {
            val settings by viewModel.settings.collectAsStateWithLifecycle()
            ProfileScreen(
                settings = settings,
                onSaveName = viewModel.settingsRepository::setProfileName,
                onSaveTheme = viewModel.settingsRepository::setTheme,
                onSaveLimit = viewModel.settingsRepository::setMonthlyLimit
            )
        }
        composable(BudgetRoutes.EXCHANGE_RATES) {
            Column(Modifier.fillMaxSize().padding(24.dp)) {
                Text("Exchange rates", style = MaterialTheme.typography.headlineMedium)
                Text("Exchange rates are currently unavailable.")
            }
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
        composable(
            route = BudgetRoutes.EXPENSE_DETAILS,
            arguments = listOf(navArgument(BudgetRoutes.EXPENSE_ID) { type = NavType.IntType })
        ) { entry ->
            val expenseId = entry.arguments?.getInt(BudgetRoutes.EXPENSE_ID)
            val expenses by viewModel.expenses.collectAsStateWithLifecycle()
            ExpenseDetailsScreen(
                expense = expenses.firstOrNull { it.id == expenseId },
                onBack = { navController.popBackStack(BudgetRoutes.EXPENSES, inclusive = false) }
            )
        }
    }
}
