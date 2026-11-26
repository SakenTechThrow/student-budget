package com.example.studentbudget.ui.exchange

import java.math.BigDecimal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TengeConversionTest {
    private val rate = BigDecimal("500")

    @Test
    fun convertsTenDollarsToFiveThousandTenge() {
        assertEquals(BigDecimal("5000"), convertToTenge("10", rate))
    }

    @Test
    fun acceptsDotAndCommaDecimals() {
        assertEquals(BigDecimal("625.00"), convertToTenge("1.25", rate))
        assertEquals(BigDecimal("625.00"), convertToTenge("1,25", rate))
    }

    @Test
    fun rejectsBlankZeroNegativeAndMalformedAmounts() {
        for (input in listOf("", " ", "0", "0.00", "-4", "1.2.3", "2e9", "1,5.0", "abc")) {
            assertNull("Input: $input", convertToTenge(input, rate))
        }
    }
}
