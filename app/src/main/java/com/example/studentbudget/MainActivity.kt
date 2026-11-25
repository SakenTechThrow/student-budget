package com.example.studentbudget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.studentbudget.data.ExpenseRepository
import com.example.studentbudget.data.local.BudgetDatabase
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
                BudgetViewModel(ExpenseRepository(dao))
            }
        }
        val viewModel = ViewModelProvider(this, factory)[BudgetViewModel::class.java]
        setContent {
            StudentBudgetTheme {
                BudgetNavHost(viewModel)
            }
        }
    }
}
