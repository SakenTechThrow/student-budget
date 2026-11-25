package com.example.studentbudget.data.settings

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

internal val Context.budgetPreferences by preferencesDataStore(name = "budget_settings")
