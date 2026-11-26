package com.example.studentbudget.ui.addexpense

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.studentbudget.data.Expense
import java.time.LocalDate
import kotlinx.coroutines.launch

/** Single form for creation and editing; never clears fields after a failed save. */
@Composable
internal fun ExpenseEditorScreen(
    expense: Expense?,
    categories: List<String>,
    onSave: suspend (String, Int, String, LocalDate) -> String?,
    onBack: () -> Unit
) {
    var title by rememberSaveable(expense?.id) { mutableStateOf(expense?.title.orEmpty()) }
    var amount by rememberSaveable(expense?.id) { mutableStateOf(expense?.amount?.toString().orEmpty()) }
    var category by rememberSaveable(expense?.id) {
        mutableStateOf(expense?.category?.displayName ?: categories.firstOrNull().orEmpty())
    }
    var dateText by rememberSaveable(expense?.id) {
        mutableStateOf((expense?.purchaseDate ?: LocalDate.now()).toString())
    }
    val purchaseDate = LocalDate.parse(dateText)
    var menuOpen by remember { mutableStateOf(false) }
    var submitted by rememberSaveable(expense?.id) { mutableStateOf(false) }
    var saving by remember { mutableStateOf(false) }
    var saveError by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val parsedAmount = parseExpenseAmount(amount)
    BackHandler(enabled = !saving) { onBack() }

    Column(
        modifier = Modifier.fillMaxSize().safeDrawingPadding().imePadding()
            .verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(if (expense == null) "Add expense" else "Edit expense",
            style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(
            value = title,
            onValueChange = { title = it; saveError = null },
            label = { Text("Expense name") },
            singleLine = true,
            isError = submitted && title.isBlank(),
            supportingText = { if (submitted && title.isBlank()) Text("Enter an expense name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it; saveError = null },
            label = { Text("Amount (₸)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = submitted && parsedAmount == null,
            supportingText = { if (submitted && parsedAmount == null) Text("Enter a positive whole number") },
            modifier = Modifier.fillMaxWidth()
        )
        Text("Category", style = MaterialTheme.typography.labelLarge)
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            enabled = categories.isNotEmpty(),
            onClick = { menuOpen = true }
        ) { Text(category.ifEmpty { "Select a category" }) }
        DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
            categories.forEach { name ->
                DropdownMenuItem(text = { Text(name) }, onClick = {
                    category = name
                    menuOpen = false
                    saveError = null
                })
            }
        }
        if (submitted && category !in categories) {
            Text("Select a category", color = MaterialTheme.colorScheme.error)
        }
        ExpenseDateField(purchaseDate) { dateText = it.toString(); saveError = null }
        if (saveError != null) {
            Text(saveError.orEmpty(), color = MaterialTheme.colorScheme.error)
        }
        Button(
            onClick = {
                submitted = true
                if (!saving && title.isNotBlank() && parsedAmount != null && category in categories) {
                    saving = true
                    scope.launch {
                        try {
                            val error = onSave(title.trim(), parsedAmount, category, purchaseDate)
                            if (error == null) onBack() else saveError = error
                        } finally {
                            saving = false
                        }
                    }
                }
            },
            enabled = !saving,
            modifier = Modifier.fillMaxWidth()
        ) { Text(if (saving) "Saving..." else "Save") }
        OutlinedButton(onClick = onBack, enabled = !saving, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
    }
}
