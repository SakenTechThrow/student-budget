package com.example.studentbudget.ui.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewModelScope
import com.example.studentbudget.data.exchange.ExchangeRateNetwork
import com.example.studentbudget.data.exchange.ExchangeRatesRepository
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class ExchangeRatesViewModel(
    private val repository: ExchangeRatesRepository
) : ViewModel() {
    private val mutableState = MutableStateFlow<ExchangeRatesState>(ExchangeRatesState.Loading)
    val state = mutableState.asStateFlow()
    private var request: Job? = null

    fun loadRates() {
        if (request?.isActive == true) return

        request = viewModelScope.launch {
            mutableState.value = ExchangeRatesState.Loading
            try {
                val rates = repository.loadRates()
                mutableState.value = if (rates.isEmpty()) {
                    ExchangeRatesState.Empty
                } else {
                    ExchangeRatesState.Success(rates)
                }
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                mutableState.value = ExchangeRatesState.Error
            }
        }
    }

    companion object {
        val factory = viewModelFactory {
            initializer { ExchangeRatesViewModel(ExchangeRateNetwork.repository) }
        }
    }
}
