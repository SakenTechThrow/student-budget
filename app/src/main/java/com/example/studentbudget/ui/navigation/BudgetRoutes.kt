package com.example.studentbudget.ui.navigation

internal object BudgetRoutes {
    const val EXPENSES = "expenses"
    const val STATISTICS = "statistics"
    const val EXCHANGE_RATES = "exchange-rates"
    const val PROFILE = "profile"
    const val ADD_EXPENSE = "expenses/add"

    const val EXPENSE_ID = "expenseId"
    const val EXPENSE_DETAILS = "expenses/details/{$EXPENSE_ID}"
    const val EDIT_EXPENSE = "expenses/edit/{$EXPENSE_ID}"

    fun expenseDetails(expenseId: Int): String = "expenses/details/$expenseId"
    fun editExpense(expenseId: Int): String = "expenses/edit/$expenseId"
}
