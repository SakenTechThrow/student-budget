package com.example.studentbudget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import com.example.studentbudget.ui.exchange.ExchangeRatesRoute
import com.example.studentbudget.ui.exchange.ExchangeRatesViewModel
import com.example.studentbudget.ui.expenses.ExpenseListItem
import com.example.studentbudget.ui.expenses.ExpenseListScreen
import com.example.studentbudget.ui.theme.StudentBudgetTheme
import kotlinx.coroutines.launch

private enum class BudgetTab(val label: Int, val icon: Int) {
    Expenses(R.string.expenses_tab, R.drawable.ic_expenses),
    ExchangeRates(R.string.exchange_rates, R.drawable.ic_exchange)
}

class MainActivity : ComponentActivity() {
    private val exchangeRatesViewModel by lazy {
        ViewModelProvider(this, ExchangeRatesViewModel.factory)[ExchangeRatesViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentBudgetTheme {
                var tab by rememberSaveable { mutableStateOf(BudgetTab.Expenses) }
                val savedTabs = rememberSaveableStateHolder()
                BackHandler(enabled = tab == BudgetTab.ExchangeRates) {
                    tab = BudgetTab.Expenses
                }

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            BudgetTab.entries.forEach { destination ->
                                NavigationBarItem(
                                    selected = tab == destination,
                                    onClick = { tab = destination },
                                    icon = { Icon(painterResource(destination.icon), contentDescription = null) },
                                    label = { Text(stringResource(destination.label)) }
                                )
                            }
                        }
                    }
                ) { padding ->
                    Box(Modifier.padding(padding).consumeWindowInsets(padding)) {
                        savedTabs.SaveableStateProvider(tab.name) {
                            when (tab) {
                                BudgetTab.Expenses -> ExpensesDemo()
                                BudgetTab.ExchangeRates -> ExchangeRatesRoute(exchangeRatesViewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpensesDemo() {
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val sampleExpenses = listOf(
        ExpenseListItem(1, stringResource(R.string.sample_bus), 120, stringResource(R.string.category_transport)),
        ExpenseListItem(2, stringResource(R.string.sample_coffee), 800, stringResource(R.string.category_food))
    )
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
