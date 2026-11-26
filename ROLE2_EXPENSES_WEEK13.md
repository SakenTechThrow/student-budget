# Student Budget — Assignment 8–9 / Role 2: Expense screens

This branch adds the **expense screens and CRUD** portion to the existing Kotlin/Jetpack Compose/MVVM application, using the team's Room DAO, repository, ViewModel and DataStore settings.

## Delivered
- Create expenses using the shared `ExpenseEditorScreen`, with title, positive whole-tenge amount, category and purchase date (today by default).
- Open an expense to see title, category, amount and purchase date.
- Edit existing expenses using the same editor; fields are prefilled from Room data.
- Delete expenses only after a confirmation dialog.
- Keep user-entered values after a failed save, and show validation messages.
- Filter by category; `All` clears the filter, while totals and category summary always include **all** expenses.
- Sort by **Newest first** (descending ID/insertion order) or **Highest amount first** (descending amount, descending ID for ties). Sorting preference persists through existing DataStore.
- React automatically to Room Flow updates for expenses, totals and summaries.
- Add unit tests for category totals, empty lists, filtering and tie-breaking.

## Boundaries
This branch implements only team role 2. Room entities/DAO and DataStore are provided by role 1. Statistics, converter and profile screens are other team members' responsibilities. Changes are on `feature/expense-crud-week13`, not on `main`.

## Check locally
```powershell
git fetch origin
git switch feature/expense-crud-week13
.\gradlew.bat :app:testDebugUnitTest
.\gradlew.bat :app:assembleDebug
```

Verify on emulator:
1. Add `Lunch`, `1500`, `Food`, today's date. Add `Bus`, `120`, `Transport`; total should be `1620 ₸`.
2. Open Lunch, edit amount to `1800`, save; total should become `1920 ₸`.
3. Select `Food`: show only Lunch, while total remains `1920 ₸`. Select `All` to restore both.
4. Switch to Highest amount first and restart the app; sorting choice must persist.
5. Change Lunch date to last month, verify the saved date after reopening.
6. Delete Bus, confirm; current expense total should be `1800 ₸`. Deleting the final record should show an empty list.
7. Enter a blank title or zero/negative/non-numeric amount: the editor must reject it.

## Integration
Keep `BudgetViewModel` shared at Activity level. `BudgetNavHost` uses the team's `ExpenseRepository` Room Flow and `SettingsRepository` sorting preference. To integrate with other contributors, merge through a pull request and resolve navigation changes together.

## Git history
This branch consists of fifteen genuine, sequential, task-focused commits made when changes were uploaded. Their timestamps are not backdated to November 25, 2026, nor spaced artificially 30–40 minutes.

## Verification status
The pure-logic unit tests were written but cannot be claimed as passed until Gradle executes them in Android Studio/CI. Confirm emulator behavior before merging.
