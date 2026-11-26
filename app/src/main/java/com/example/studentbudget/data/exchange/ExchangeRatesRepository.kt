package com.example.studentbudget.data.exchange

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

internal class ExchangeRatesRepository(
    private val fetchRates: suspend (String) -> List<FrankfurterRate>
) {
    suspend fun loadRates(): List<ExchangeRate> = coroutineScope {
        ExchangeCurrency.entries.map { currency ->
            async {
                fetchRates(currency.name).mapNotNull { rate ->
                    rate.toExchangeRate(currency)
                }.maxByOrNull { it.date }
            }
        }.awaitAll().filterNotNull()
    }

    private fun FrankfurterRate.toExchangeRate(currency: ExchangeCurrency): ExchangeRate? {
        val price = rate ?: return null
        val rateDate = date?.takeIf { it.isNotBlank() } ?: return null

        if (base != currency.name || quote != "KZT" || price.signum() <= 0) return null

        return ExchangeRate(currency, price, rateDate)
    }
}
