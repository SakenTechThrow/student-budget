package com.example.studentbudget.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val store: DataStore<Preferences>) {
    constructor(context: Context) : this(context.applicationContext.budgetPreferences)

    val settings: Flow<BudgetSettings> = store.data.map { preferences ->
        BudgetSettings(
            profileName = preferences[PROFILE_NAME].orEmpty(),
            theme = BudgetTheme.entries.firstOrNull { it.name == preferences[THEME] }
                ?: BudgetTheme.SYSTEM,
            expenseSort = ExpenseSort.entries.firstOrNull { it.name == preferences[EXPENSE_SORT] }
                ?: ExpenseSort.NEWEST_FIRST,
            monthlyLimit = preferences[MONTHLY_LIMIT]?.takeIf { it > 0 }
        )
    }

    suspend fun setProfileName(value: String) {
        store.edit { it[PROFILE_NAME] = value.trim() }
    }

    suspend fun setTheme(value: BudgetTheme) {
        store.edit { it[THEME] = value.name }
    }

    suspend fun setExpenseSort(value: ExpenseSort) {
        store.edit { it[EXPENSE_SORT] = value.name }
    }

    suspend fun setMonthlyLimit(value: Long?) {
        require(value == null || value > 0) { "Monthly limit must be positive" }
        store.edit { preferences ->
            if (value == null) preferences.remove(MONTHLY_LIMIT)
            else preferences[MONTHLY_LIMIT] = value
        }
    }

    private companion object {
        val PROFILE_NAME = stringPreferencesKey("profile_name")
        val THEME = stringPreferencesKey("theme")
        val EXPENSE_SORT = stringPreferencesKey("expense_sort")
        val MONTHLY_LIMIT = longPreferencesKey("monthly_limit")
    }
}
