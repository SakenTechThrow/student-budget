package com.example.studentbudget.ui.statistics

import com.example.studentbudget.data.Expense
import com.example.studentbudget.data.ExpenseCategory
import com.example.studentbudget.data.ExpenseRepository
import com.example.studentbudget.data.FakeExpenseDao
import com.example.studentbudget.ui.profile.BudgetStatus
import com.example.studentbudget.ui.profile.MonthlyBudget
import java.time.YearMonth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Test

class MonthlySpendingFlowTest {
    @Test
    fun updatesBothMonthsAndTheCurrentBudgetFromRepositoryChanges() = runBlocking {
        val repository = ExpenseRepository(FakeExpenseDao())
        val month = YearMonth.of(2026, 1)
        val previous = month.minusMonths(1)
        val emissions = Channel<List<Expense>>()
        val collector = launch { repository.expenses.collect { emissions.send(it) } }

        suspend fun assertTotals(current: Long, past: Long, balance: Long, status: BudgetStatus) {
            val expenses = withTimeout(5_000) { emissions.receive() }
            val monthlyTotal = expenses.statisticsFor(month).total
            val budget = MonthlyBudget(monthlyTotal, 2_000)
            assertEquals(current, monthlyTotal)
            assertEquals(past, expenses.statisticsFor(previous).total)
            assertEquals(balance, budget.balance)
            assertEquals(status, budget.status)
        }

        try {
            assertTotals(0, 0, 2_000, BudgetStatus.WITHIN_LIMIT)
            val lunch = repository.addExpense("Lunch", 1_620, ExpenseCategory.FOOD, month.atDay(1))
            assertTotals(1_620, 0, 380, BudgetStatus.NEAR_LIMIT)
            val edited = lunch.copy(amount = 2_100)
            repository.updateExpense(edited)
            assertTotals(2_100, 0, -100, BudgetStatus.EXCEEDED)
            repository.updateExpense(edited.copy(purchaseDate = previous.atEndOfMonth()))
            assertTotals(0, 2_100, 2_000, BudgetStatus.WITHIN_LIMIT)
            repository.deleteExpense(edited.id)
            assertTotals(0, 0, 2_000, BudgetStatus.WITHIN_LIMIT)
        } finally {
            collector.cancel()
        }
    }
}
