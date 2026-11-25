package com.example.studentbudget.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import com.example.studentbudget.data.settings.BudgetSettings
import java.io.IOException
import kotlinx.coroutines.launch

@Composable
internal fun ProfileScreen(
    settings: BudgetSettings,
    onSaveName: suspend (String) -> Unit
) {
    var nameDraft by rememberSaveable(settings.profileName) { mutableStateOf(settings.profileName) }
    var nameError by rememberSaveable { mutableStateOf(false) }
    var savingName by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { Text("Profile", style = MaterialTheme.typography.headlineMedium) }
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                    Box(Modifier.size(56.dp), contentAlignment = Alignment.Center) {
                        Text(profileInitials(settings.profileName).ifEmpty { "?" })
                    }
                }
                Text(settings.profileName.ifBlank { "Local profile" })
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nameDraft,
                    onValueChange = { nameDraft = it; nameError = false },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Name") },
                    singleLine = true,
                    enabled = !savingName,
                    isError = nameError,
                    supportingText = if (nameError) {
                        { Text("Could not save your name. Try again.") }
                    } else null
                )
                Button(
                    enabled = !savingName && nameDraft.trim() != settings.profileName,
                    onClick = {
                        scope.launch {
                            savingName = true
                            try {
                                onSaveName(nameDraft)
                            } catch (_: IOException) {
                                nameError = true
                            } finally {
                                savingName = false
                            }
                        }
                    }
                ) { Text("Save name") }
            }
        }
    }
}
