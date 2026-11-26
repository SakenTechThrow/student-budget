package com.example.studentbudget.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.studentbudget.data.settings.BudgetTheme
import java.io.IOException
import kotlinx.coroutines.launch

@Composable
internal fun ThemeOptions(theme: BudgetTheme, onSelect: suspend (BudgetTheme) -> Unit) {
    val scope = rememberCoroutineScope()
    var saving by remember { mutableStateOf(false) }
    var saveError by rememberSaveable { mutableStateOf(false) }
    Column(Modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Theme", style = MaterialTheme.typography.titleMedium)
        BudgetTheme.entries.forEach { option ->
            Row(
                modifier = Modifier.fillMaxWidth().selectable(
                    selected = option == theme,
                    enabled = !saving,
                    role = Role.RadioButton,
                    onClick = {
                        scope.launch {
                            saving = true
                            saveError = false
                            try {
                                onSelect(option)
                            } catch (_: IOException) {
                                saveError = true
                            } finally {
                                saving = false
                            }
                        }
                    }
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = option == theme, onClick = null)
                Text(when (option) {
                    BudgetTheme.SYSTEM -> "System"
                    BudgetTheme.LIGHT -> "Light"
                    BudgetTheme.DARK -> "Dark"
                })
            }
        }
        if (saveError) Text("Could not update the theme. Try again.", color = MaterialTheme.colorScheme.error)
    }
}
