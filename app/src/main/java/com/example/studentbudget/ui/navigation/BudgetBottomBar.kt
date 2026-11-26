package com.example.studentbudget.ui.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

internal enum class BudgetTab(val route: String, val label: String, val symbol: String) {
    EXPENSES(BudgetRoutes.EXPENSES, "Expenses", "₸"),
    STATISTICS(BudgetRoutes.STATISTICS, "Statistics", "▥"),
    EXCHANGE(BudgetRoutes.EXCHANGE_RATES, "Exchange rates", "⇄"),
    PROFILE(BudgetRoutes.PROFILE, "Profile", "◎")
}

@Composable
internal fun BudgetBottomBar(current: BudgetTab, onSelect: (BudgetTab) -> Unit) {
    NavigationBar {
        BudgetTab.entries.forEach { tab ->
            NavigationBarItem(
                selected = current == tab,
                onClick = { if (tab != current) onSelect(tab) },
                icon = { Text(tab.symbol, modifier = Modifier.clearAndSetSemantics { }) },
                label = {
                    Text(tab.label, textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
            )
        }
    }
}
