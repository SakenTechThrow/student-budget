package com.example.studentbudget.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.studentbudget.R
import com.example.studentbudget.data.ExpenseCategory
import com.example.studentbudget.data.settings.ExpenseSort
import com.example.studentbudget.ui.addexpense.AddExpenseScreen
import com.example.studentbudget.ui.addexpense.ExpenseEditorScreen
import com.example.studentbudget.ui.details.ExpenseDetailsScreen
import com.example.studentbudget.ui.exchange.ExchangeRatesRoute
import com.example.studentbudget.ui.exchange.ExchangeRatesViewModel
import com.example.studentbudget.ui.expenses.ExpenseListItem
import com.example.studentbudget.ui.expenses.ExpenseListScreen
import com.example.studentbudget.ui.expenses.categoryTotals
import com.example.studentbudget.ui.expenses.visibleExpenses
import com.example.studentbudget.viewmodel.BudgetViewModel
import kotlinx.coroutines.launch

private enum class BudgetTab(val route: String, val label: Int, val icon: Int) {
    Expenses(BudgetRoutes.EXPENSES, R.string.expenses_tab, R.drawable.ic_expenses),
    ExchangeRates(BudgetRoutes.EXCHANGE_RATES, R.string.exchange_rates, R.drawable.ic_exchange)
}

@Composable
internal fun BudgetNavHost(
    viewModel: BudgetViewModel,
    exchangeRatesViewModel: ExchangeRatesViewModel
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (BudgetTab.entries.any { it.route == currentRoute }) {
                NavigationBar {
                    BudgetTab.entries.forEach { tab ->
                        NavigationBarItem(
                            selected = currentRoute == tab.route,
                            onClick = {
                                if (currentRoute != tab.route) {
                                    navController.navigate(tab.route) {
                                        popUpTo(BudgetRoutes.EXPENSES) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = { Icon(painterResource(tab.icon), contentDescription = null) },
                            label = { Text(stringResource(tab.label)) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = BudgetRoutes.EXPENSES,
            modifier = Modifier.padding(padding).consumeWindowInsets(padding)
        ) {
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
            composable(BudgetRoutes.EXCHANGE_RATES) {
                Box(Modifier.safeDrawingPadding()) {
                    ExchangeRatesRoute(exchangeRatesViewModel)
                }
            }
        }
    }
}
