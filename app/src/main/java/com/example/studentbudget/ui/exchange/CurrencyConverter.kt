package com.example.studentbudget.ui.exchange

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.studentbudget.R
import com.example.studentbudget.data.exchange.ExchangeCurrency
import com.example.studentbudget.data.exchange.ExchangeRate

@Composable
internal fun CurrencyConverter(
    rates: List<ExchangeRate>,
    currency: ExchangeCurrency,
    onCurrencyChange: (ExchangeCurrency) -> Unit,
    amount: String,
    onAmountChange: (String) -> Unit
) {
    val selectedRate = rates.firstOrNull { it.currency == currency } ?: rates.first()
    val converted = convertToTenge(amount, selectedRate.kztPerUnit)

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.exchange_converter),
            modifier = Modifier.semantics { heading() },
            style = MaterialTheme.typography.titleLarge
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            rates.forEach { rate ->
                FilterChip(
                    selected = rate.currency == selectedRate.currency,
                    onClick = { onCurrencyChange(rate.currency) },
                    label = { Text(rate.currency.name) }
                )
            }
        }
        OutlinedTextField(
            value = amount,
            onValueChange = onAmountChange,
            label = { Text(stringResource(R.string.exchange_amount, selectedRate.currency.name)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        if (converted != null) {
            Text(
                text = stringResource(
                    R.string.exchange_result,
                    formatExchangeAmount(converted, fractionDigits = 2)
                ),
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}
