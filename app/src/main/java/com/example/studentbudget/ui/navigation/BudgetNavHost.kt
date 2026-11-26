package com.example.studentbudget.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.studentbudget.data.ExpenseCategory
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.studentbudget.data.settings.ExpenseSort
import com.example.studentbudget.ui.addexpense.ExpenseEditorScreen
import com.example.studentbudget.ui.expenses.categoryTotals
import com.example.studentbudget.ui.expenses.visibleExpenses
import kotlinx.coroutines.launch
import com.example.studentbudget.ui.details.ExpenseDetailsScreen
import com.example.studentbudget.ui.exchange.ExchangeRatesRoute
import com.example.studentbudget.ui.exchange.ExchangeRatesViewModel
import com.example.studentbudget.ui.expenses.ExpenseListItem
import com.example.studentbudget.ui.expenses.ExpenseListScreen
import com.example.studentbudget.ui.expenses.MonthlyBudgetCard
import com.example.studentbudget.ui.profile.MonthlyBudget
import com.example.studentbudget.ui.profile.ProfileScreen
import com.example.studentbudget.ui.statistics.StatisticsScreen
import com.example.studentbudget.viewmodel.BudgetViewModel

@Composable
internal fun BudgetNavHost(
    viewModel: BudgetViewModel,
    exchangeRatesViewModel: () -> ExchangeRatesViewModel
) {
    val navController = rememberNavController()
    val entry by navController.currentBackStackEntryAsState()
    val tab = BudgetTab.entries.firstOrNull { it.route == entry?.destination?.route }
    Scaffold(
        bottomBar = {
            if (tab != null) {
                BudgetBottomBar(tab) { target ->
                    navController.navigate(target.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    ) { insets ->
        NavHost(
            navController = navController,
            startDestination = BudgetRoutes.EXPENSES,
            modifier = Modifier.padding(insets).consumeWindowInsets(insets)
        ) {
            composable(BudgetRoutes.EXPENSES) {
                val expenses by viewModel.expenses.collectAsStateWithLifecycle()
                val total by viewModel.totalAmount.collectAsStateWithLifecycle()
                val summaries by viewModel.monthlyStatistics.collectAsStateWithLifecycle()
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
                    onSortChange = { sort: ExpenseSort -> scope.launch { viewModel.setExpenseSort(sort) } },
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
                ExchangeRatesRoute(exchangeRatesViewModel())
            }
            composable(BudgetRoutes.ADD_EXPENSE) {
                val categories = ExpenseCategory.entries.associateBy(ExpenseCategory::displayName)
                ExpenseEditorScreen(
                    expense = null,
                    categories = categories.keys.toList(),
                    onSave = { title, amount, categoryName, date ->
                        val category = categories[categoryName]
                        if (category == null) "Choose a category"
                        else viewModel.addExpense(title, amount.toString(), category, date)
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
                    onBack = { navController.popBackStack(BudgetRoutes.EXPENSES, inclusive = false) },
                    onEdit = {
                        if (expenseId != null) navController.navigate(BudgetRoutes.editExpense(expenseId))
                    },
                    onDelete = {
                        if (expenseId != null) viewModel.deleteExpense(expenseId) else false
                    }
                )
            }
            composable(
                route = BudgetRoutes.EDIT_EXPENSE,
                arguments = listOf(navArgument(BudgetRoutes.EXPENSE_ID) { type = NavType.IntType })
            ) { entry ->
                val expenseId = entry.arguments?.getInt(BudgetRoutes.EXPENSE_ID)
                val expenses by viewModel.expenses.collectAsStateWithLifecycle()
                val expense = expenses.firstOrNull { it.id == expenseId }
                if (expense == null) {
                    androidx.compose.material3.Text("Expense not found")
                } else {
                    val categories = ExpenseCategory.entries.associateBy(ExpenseCategory::displayName)
                    ExpenseEditorScreen(
                        expense = expense,
                        categories = categories.keys.toList(),
                        onSave = { title, amount, categoryName, date ->
                            val category = categories[categoryName]
                            if (category == null) "Choose a category"
                            else viewModel.updateExpense(
                                expense.copy(
                                    title = title,
                                    amount = amount,
                                    category = category,
                                    purchaseDate = date
                                )
                            )
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
