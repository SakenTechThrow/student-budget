package com.example.studentbudget.data.exchange

import retrofit2.http.GET
import retrofit2.http.Query

internal interface FrankfurterApi {
    @GET("v2/rates?quotes=KZT")
    suspend fun getRates(@Query("base") base: String): List<FrankfurterRate>
}
