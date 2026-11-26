package com.example.studentbudget.data.exchange

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object ExchangeRateNetwork {
    val repository: ExchangeRatesRepository by lazy {
        val api = Retrofit.Builder()
            .baseUrl("https://api.frankfurter.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FrankfurterApi::class.java)

        ExchangeRatesRepository(api)
    }
}
