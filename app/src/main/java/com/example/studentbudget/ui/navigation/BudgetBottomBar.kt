package com.example.studentbudget.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.studentbudget.R

internal enum class BudgetTab(val route: String, val label: Int) {
    EXPENSES(BudgetRoutes.EXPENSES, R.string.expenses_tab),
    STATISTICS(BudgetRoutes.STATISTICS, R.string.statistics_tab),
    EXCHANGE(BudgetRoutes.EXCHANGE_RATES, R.string.exchange_rates),
    PROFILE(BudgetRoutes.PROFILE, R.string.profile_tab)
}

@Composable
internal fun BudgetBottomBar(current: BudgetTab, onSelect: (BudgetTab) -> Unit) {
    NavigationBar {
        BudgetTab.entries.forEach { tab ->
            NavigationBarItem(
                selected = current == tab,
                onClick = { if (tab != current) onSelect(tab) },
                icon = {
                    when (tab) {
                        BudgetTab.EXPENSES -> Icon(painterResource(R.drawable.ic_expenses), contentDescription = null)
                        BudgetTab.EXCHANGE -> Icon(painterResource(R.drawable.ic_exchange), contentDescription = null)
                        BudgetTab.STATISTICS -> Text("▥", modifier = Modifier.clearAndSetSemantics { })
                        BudgetTab.PROFILE -> Text("◎", modifier = Modifier.clearAndSetSemantics { })
                    }
                },
                label = {
                    Text(stringResource(tab.label), textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
            )
        }
    }
}
