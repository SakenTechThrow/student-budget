# Part 2 — Data & ViewModel

Branch: `feature/budget-state`

## Files
- `data/Expense.kt`: immutable expense model with `Int` ID and amount (whole tenge).
- `data/ExpenseCategory.kt`: enum of categories.
- `data/ExpenseRepository.kt`: in-memory StateFlow, two seed records (Bus 120 ₸ + Coffee 800 ₸), creation and lookup by ID.
- `viewmodel/BudgetViewModel.kt`: expenses and totalAmount exposed as StateFlow, addExpense validation, findExpenseById.
- Unit tests: seed total 920 ₸, adding Lunch 1500 ₸ gives 2420 ₸, rejects invalid inputs and IDs are unique.

## UI team integration (feature/expense-list-v2)
Their `ExpenseListScreen` accepts `List<ExpenseListItem>` where ID and amount are Int. Convert your shared ViewModel data before passing it:

```kotlin
val entries by budgetViewModel.expenses.collectAsState()
ExpenseListScreen(
    expenses = entries.map {
        ExpenseListItem(it.id, it.title, it.amount, it.category.displayName)
    },
    onAddExpense = { /* navigate to add screen */ },
    onExpenseClick = { id -> /* navigate to details with id */ }
)
```

The UI currently recalculates a total from entries, while `BudgetViewModel.totalAmount` separately exposes the required total. Both use the same repository data.

## Add screen API
`budgetViewModel.addExpense(title, amountText, ExpenseCategory.FOOD)` returns `null` on success, otherwise a user-visible error message. Amount must be a whole number greater than zero.

## Details screen API
`budgetViewModel.findExpenseById(expenseId)` returns `Expense?`.

Keep one shared `BudgetViewModel` instance for all three screens. Creating multiple instances resets sample records in this in-memory implementation.

## Tests
Run `./gradlew :app:testDebugUnitTest` (Windows: `gradlew.bat :app:testDebugUnitTest`).

The repository cannot be built in this chat environment with the Android SDK; tests have been authored but not executed. Commit timestamps are genuine GitHub write times, not backdated.
