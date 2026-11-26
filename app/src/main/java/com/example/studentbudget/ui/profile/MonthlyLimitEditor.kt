package com.example.studentbudget.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import java.io.IOException
import kotlinx.coroutines.launch

@Composable
internal fun MonthlyLimitEditor(limit: Long?, onSave: suspend (Long?) -> Unit) {
    var draft by rememberSaveable(limit) { mutableStateOf(limit?.toString().orEmpty()) }
    var saving by remember { mutableStateOf(false) }
    var saveError by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val amount = draft.toMonthlyLimit()
    val invalid = draft.isNotBlank() && amount == null
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = draft,
            onValueChange = {
                draft = it
                saveError = false
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Monthly limit (₸)") },
            supportingText = {
                Text(when {
                    invalid -> "Enter a positive whole-tenge amount."
                    saveError -> "Could not save the limit. Try again."
                    else -> "Leave blank to turn off the limit."
                })
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            isError = invalid || saveError,
            enabled = !saving
        )
        Button(
            enabled = !saving && !invalid && amount != limit,
            onClick = {
                scope.launch {
                    saving = true
                    try {
                        onSave(amount)
                    } catch (_: IOException) {
                        saveError = true
                    } finally {
                        saving = false
                    }
                }
            }
        ) { Text("Save limit") }
    }
}
