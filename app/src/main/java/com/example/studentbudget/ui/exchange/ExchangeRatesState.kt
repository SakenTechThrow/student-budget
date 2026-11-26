package com.example.studentbudget.ui.exchange

import com.example.studentbudget.data.exchange.ExchangeRate

internal sealed interface ExchangeRatesState {
    data object Loading : ExchangeRatesState
    data object Empty : ExchangeRatesState
    data object Error : ExchangeRatesState
    data class Success(val rates: List<ExchangeRate>) : ExchangeRatesState
}
