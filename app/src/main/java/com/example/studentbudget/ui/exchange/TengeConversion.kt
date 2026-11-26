package com.example.studentbudget.ui.exchange

import java.math.BigDecimal
import java.text.NumberFormat

private val decimalAmount = Regex("[0-9]+([.,][0-9]+)?")

internal fun convertToTenge(input: String, rate: BigDecimal): BigDecimal? {
    val text = input.trim()
    if (!decimalAmount.matches(text)) return null

    val amount = text.replace(',', '.').toBigDecimalOrNull() ?: return null
    return amount.takeIf { it.signum() > 0 }?.multiply(rate)
}

internal fun formatExchangeAmount(amount: BigDecimal, fractionDigits: Int): String =
    NumberFormat.getNumberInstance().apply {
        maximumFractionDigits = fractionDigits
    }.format(amount)
