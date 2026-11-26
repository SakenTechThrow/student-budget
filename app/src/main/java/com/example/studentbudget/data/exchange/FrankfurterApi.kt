package com.example.studentbudget.data.exchange

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import retrofit2.http.GET
import retrofit2.http.Query

internal interface FrankfurterApi {
    @GET("v2/rates?quotes=KZT")
    suspend fun getRates(@Query("base") base: String): List<FrankfurterRate>
}

internal data class FrankfurterRate(
    @SerializedName("base") val base: String?,
    @SerializedName("quote") val quote: String?,
    @SerializedName("rate") val rate: BigDecimal?,
    @SerializedName("date") val date: String?
)
