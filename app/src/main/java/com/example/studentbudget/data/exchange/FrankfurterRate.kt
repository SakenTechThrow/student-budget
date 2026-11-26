package com.example.studentbudget.data.exchange

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

internal data class FrankfurterRate(
    @SerializedName("base") val base: String?,
    @SerializedName("quote") val quote: String?,
    @SerializedName("rate") val rate: BigDecimal?,
    @SerializedName("date") val date: String?
)
