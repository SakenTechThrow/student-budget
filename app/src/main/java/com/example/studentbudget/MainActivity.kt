package com.example.studentbudget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.luminance
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.studentbudget.data.ExpenseRepository
import com.example.studentbudget.data.local.BudgetDatabase
import com.example.studentbudget.data.settings.SettingsRepository
import com.example.studentbudget.ui.navigation.BudgetNavHost
import com.example.studentbudget.ui.theme.StudentBudgetTheme
import com.example.studentbudget.viewmodel.BudgetViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val factory = viewModelFactory {
            initializer {
                val dao = BudgetDatabase.getInstance(applicationContext).expenseDao()
                BudgetViewModel(
                    repository = ExpenseRepository(dao),
                    settingsRepository = SettingsRepository(applicationContext)
                )
            }
        }
        val viewModel = ViewModelProvider(this, factory)[BudgetViewModel::class.java]
        setContent {
            val settings by viewModel.settings.collectAsStateWithLifecycle()
            StudentBudgetTheme(theme = settings.theme) {
                val lightBackground = MaterialTheme.colorScheme.background.luminance() > 0.5f
                SideEffect {
                    WindowCompat.getInsetsController(window, window.decorView).apply {
                        isAppearanceLightStatusBars = lightBackground
                        isAppearanceLightNavigationBars = lightBackground
                    }
                }
                BudgetNavHost(viewModel)
            }
        }
    }
}
