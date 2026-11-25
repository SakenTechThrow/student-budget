package com.example.studentbudget.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberSaveable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.studentbudget.data.ExpenseCategory
import com.example.studentbudget.data.settings.ExpenseSort
import com.example.studentbudget.ui.addexpense.AddExpenseScreen
import com.example.studentbudget.ui.details.ExpenseDetailsScreen
import com.example.studentbudget.ui.expenses.ExpenseListItem
import com.example.studentbudget.ui.expenses.ExpenseListScreen
import com.example.studentbudget.ui.expenses.categoryTotals
import com.example.studentbudget.ui.expenses.visibleExpenses
import com.example.studentbudget.viewmodel.BudgetViewModel
import kotlinx.coroutines.launch

@Composable
internal fun BudgetNavHost(viewModel: BudgetViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = BudgetRoutes.EXPENSES) {
        composable(BudgetRoutes.EXPENSES) {
            val expenses by viewModel.expenses.collectAsStateWithLifecycle()
            val total by viewModel.totalAmount.collectAsStateWithLifecycle()
            val settings by viewModel.settings.collectAsStateWithLifecycle()
            var categoryName by rememberSaveable { mutableStateOf<String?>(null) }
            val selectedCategory = ExpenseCategory.entries.firstOrNull { it.displayName == categoryName }
            val visible = visibleExpenses(expenses, selectedCategory, settings.expenseSort)
            val summary = categoryTotals(expenses).mapKeys { it.key.displayName }
            val scope = rememberCoroutineScope()
            ExpenseListScreen(
                expenses = visible.map { expense ->
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
                categorySummary = summary,
                selectedCategory = categoryName,
                onCategorySelected = { categoryName = it },
                sort = settings.expenseSort,
                onSortChange = { sort: ExpenseSort -> scope.launch { viewModel.setExpenseSort(sort) } }
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
