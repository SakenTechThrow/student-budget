package com.example.studentbudget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.studentbudget.ui.navigation.BudgetNavHost
import com.example.studentbudget.ui.theme.StudentBudgetTheme
import com.example.studentbudget.viewmodel.BudgetViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = ViewModelProvider(this)[BudgetViewModel::class.java]
        setContent {
            StudentBudgetTheme {
                BudgetNavHost(viewModel)
            }
        }
    }
}
