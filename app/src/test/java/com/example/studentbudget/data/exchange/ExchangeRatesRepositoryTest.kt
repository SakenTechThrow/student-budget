package com.example.studentbudget.data.exchange

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.math.BigDecimal
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExchangeRatesRepositoryTest {
    @Test
    fun mapsBothRatesFromJsonAndKeepsEachDate() = runBlocking {
        val repository = ExchangeRatesRepository { base ->
            when (base) {
                "USD" -> decode("""[{"base":"USD","quote":"KZT","rate":500.12,"date":"2026-11-25"}]""")
                "EUR" -> decode("""[{"base":"EUR","quote":"KZT","rate":560.45,"date":"2026-11-24"}]""")
                else -> error("Unexpected base currency: $base")
            }
        }

        assertEquals(
            listOf(
                ExchangeRate(ExchangeCurrency.USD, BigDecimal("500.12"), "2026-11-25"),
                ExchangeRate(ExchangeCurrency.EUR, BigDecimal("560.45"), "2026-11-24")
            ),
            repository.loadRates()
        )
    }

    @Test
    fun returnsTheAvailableCurrencyWhenTheOtherResponseIsEmpty() = runBlocking {
        val repository = ExchangeRatesRepository { base ->
            if (base == "EUR") {
                decode("""[{"base":"EUR","quote":"KZT","rate":560,"date":"2026-11-25"}]""")
            } else {
                decode("[]")
            }
        }

        assertEquals(
            listOf(ExchangeRate(ExchangeCurrency.EUR, BigDecimal("560"), "2026-11-25")),
            repository.loadRates()
        )
    }

    @Test
    fun returnsAnEmptyListForSuccessfulEmptyResponses() = runBlocking {
        val repository = ExchangeRatesRepository { decode("[]") }

        assertTrue(repository.loadRates().isEmpty())
    }

    @Test(expected = IOException::class)
    fun propagatesNetworkFailure() {
        runBlocking {
            ExchangeRatesRepository { throw IOException("Offline") }.loadRates()
        }
    }

    private fun decode(json: String): List<FrankfurterRate> =
        Gson().fromJson(json, object : TypeToken<List<FrankfurterRate>>() {})
}
