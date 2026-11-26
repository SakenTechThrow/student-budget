package com.example.studentbudget.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.studentbudget.data.settings.BudgetTheme

private val LightColors = lightColorScheme(
    primary = Color(0xFF23664F),
    primaryContainer = Color(0xFFB4EFD6),
    onPrimaryContainer = Color(0xFF002117)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF98D3BB),
    primaryContainer = Color(0xFF004D39),
    onPrimaryContainer = Color(0xFFB4EFD6)
)

@Composable
fun StudentBudgetTheme(
    theme: BudgetTheme = BudgetTheme.SYSTEM,
    content: @Composable () -> Unit
) {
    val darkTheme = when (theme) {
        BudgetTheme.SYSTEM -> isSystemInDarkTheme()
        BudgetTheme.LIGHT -> false
        BudgetTheme.DARK -> true
    }
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(colorScheme = colors, content = content)
}
