package com.example.studentbudget.data.exchange

import java.math.BigDecimal

enum class ExchangeCurrency {
    USD,
    EUR
}

data class ExchangeRate(
    val currency: ExchangeCurrency,
    val kztPerUnit: BigDecimal,
    val date: String
)
