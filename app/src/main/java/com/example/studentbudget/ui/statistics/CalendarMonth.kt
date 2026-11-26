package com.example.studentbudget.ui.statistics

import java.time.YearMonth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow

internal fun calendarMonths() = flow {
    while (true) {
        emit(YearMonth.now())
        delay(1_000)
    }
}.distinctUntilChanged()
