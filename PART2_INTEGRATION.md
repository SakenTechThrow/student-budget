# Local persistence contracts

`MainActivity` creates the activity-scoped `BudgetViewModel` with a Room-backed `ExpenseRepository` and a Preferences DataStore `SettingsRepository`.

## Expenses

- Collect `BudgetViewModel.expenses` for the saved rows. It starts empty while the first query completes. The Room repository emits an updated list after an insert, update, or delete.
- `Expense.purchaseDate` is a `LocalDate`. Pass the selected date when creating or updating a record. Room stores it as an epoch day so it is independent of time zone offsets.
- `ExpenseRepository.addExpense` inserts and returns the created expense. `findById` returns the record or null.
- `ExpenseRepository.updateExpense(expense)` and `deleteExpense(id)` return whether a stored row was changed. The ViewModel for the edit/delete UI can call these suspend functions in its coroutine scope.
- The initial database is empty. Old app versions kept expenses only in memory, so there is no prior on-device database to migrate.

## Preferences

- Collect `BudgetViewModel.settingsRepository.settings` for `BudgetSettings`.
- Save a name, theme, sort order, or limit with the matching suspend setter. `setMonthlyLimit(null)` clears the limit. Amounts are whole tenge.
- The default settings are an empty profile name, system theme, newest-first sorting, and no monthly limit.
