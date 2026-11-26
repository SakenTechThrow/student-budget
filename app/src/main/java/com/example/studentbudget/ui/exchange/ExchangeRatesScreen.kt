package com.example.studentbudget.ui.exchange

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studentbudget.R
import com.example.studentbudget.data.exchange.ExchangeCurrency
import com.example.studentbudget.data.exchange.ExchangeRate

@Composable
internal fun ExchangeRatesRoute(viewModel: ExchangeRatesViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var currency by rememberSaveable { mutableStateOf(ExchangeCurrency.USD) }
    var amount by rememberSaveable { mutableStateOf("") }
    LaunchedEffect(viewModel) {
        viewModel.loadRates()
    }

    ExchangeRatesScreen(
        state = state,
        onRetry = viewModel::loadRates,
        currency = currency,
        onCurrencyChange = { currency = it },
        amount = amount,
        onAmountChange = { amount = it }
    )
}

@Composable
private fun ExchangeRatesScreen(
    state: ExchangeRatesState,
    onRetry: () -> Unit,
    currency: ExchangeCurrency,
    onCurrencyChange: (ExchangeCurrency) -> Unit,
    amount: String,
    onAmountChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.exchange_rates),
            modifier = Modifier.semantics { heading() },
            style = MaterialTheme.typography.headlineMedium
        )
        when (state) {
            ExchangeRatesState.Loading -> Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                Text(stringResource(R.string.exchange_loading))
            }
            ExchangeRatesState.Empty -> Text(stringResource(R.string.exchange_empty))
            ExchangeRatesState.Error -> {
                Text(stringResource(R.string.exchange_error))
                Button(onClick = onRetry) {
                    Text(stringResource(R.string.exchange_retry))
                }
            }
            is ExchangeRatesState.Success -> {
                state.rates.forEach { RateCard(it) }
                CurrencyConverter(
                    rates = state.rates,
                    currency = currency,
                    onCurrencyChange = onCurrencyChange,
                    amount = amount,
                    onAmountChange = onAmountChange
                )
            }
        }
    }
}

@Composable
private fun RateCard(rate: ExchangeRate) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.exchange_rate_value,
                    rate.currency.name,
                    formatExchangeAmount(rate.kztPerUnit, fractionDigits = 4)
                ),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(R.string.exchange_rate_date, rate.date),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
