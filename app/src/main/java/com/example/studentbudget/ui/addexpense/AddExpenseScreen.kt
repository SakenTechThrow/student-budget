package com.example.studentbudget.ui.addexpense

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AddExpenseScreen(
    categories: List<String>,
    onSave: (String, Double, String) -> Boolean,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(categories.firstOrNull().orEmpty()) }
    var categoryMenuOpen by remember { mutableStateOf(false) }
    var submitted by remember { mutableStateOf(false) }
    val parsedAmount = parseExpenseAmount(amount)
    val titleError = submitted && title.isBlank()
    val amountError = submitted && parsedAmount == null
    val categoryError = submitted && category !in categories

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Add expense", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
            },
            label = { Text("Expense name") },
            singleLine = true,
            isError = titleError,
            supportingText = { if (titleError) Text("Enter an expense name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = amount,
            onValueChange = {
                amount = it
            },
            label = { Text("Amount (₸)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            isError = amountError,
            supportingText = { if (amountError) Text("Enter a valid amount greater than 0") },
            modifier = Modifier.fillMaxWidth()
        )
        Column {
            Text("Category", style = MaterialTheme.typography.labelLarge)
            OutlinedButton(
                onClick = { categoryMenuOpen = true },
                enabled = categories.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(category.ifEmpty { "Select a category" })
            }
            DropdownMenu(
                expanded = categoryMenuOpen,
                onDismissRequest = { categoryMenuOpen = false }
            ) {
                categories.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            category = item
                            categoryMenuOpen = false
                        }
                    )
                }
            }
            if (categoryError) {
                Text("Select a category", color = MaterialTheme.colorScheme.error)
            }
        }
        Button(
            onClick = {
                submitted = true
                if (title.isNotBlank() && parsedAmount != null && category in categories) {
                    if (onSave(title.trim(), parsedAmount, category)) {
                        onBack()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddExpensePreview() {
    MaterialTheme {
        AddExpenseScreen(
            categories = listOf("Food", "Transport", "Study", "Other"),
            onSave = { _, _, _ -> false },
            onBack = {}
        )
    }
}
