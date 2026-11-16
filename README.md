# student-budget
Student Budget — a Kotlin and Jetpack Compose group project for Mobile Development.

## Add expense

Branch: `feature/add-expense`.

`AddExpenseScreen` has an expense name, amount in tenge, category selector, Save and Back buttons. Empty names and invalid amounts show errors after Save. Amounts must be greater than zero and can have up to two decimal places, using a point or comma.

The screen receives category names and two callbacks:

- `onSave(title, amount, category)` saves through the shared `BudgetViewModel` and returns `true` only after a successful save.
- `onBack()` returns to the expense list. It is called after a successful save, by Back, or by the system back action.

A failed save keeps the entered values and shows an error. Form values survive rotation. The screen scrolls when the keyboard is open or the display is small.

`BudgetViewModel` and navigation are being developed by other teammates. Connect the callbacks after their code is available. The main activity is unchanged. Open `AddExpensePreview` in Android Studio to view the form.

### Manual checks

1. Press Save with an empty name: the name error appears.
2. Enter `0`, a negative value, or text as the amount: the amount error appears.
3. Enter `Lunch`, `1500`, and a category: Save passes the values to the callback.
4. Return `true` from the callback: the screen calls `onBack` once.
5. Return `false`: the form remains open and shows a save error.
6. Rotate the device: the entered name, amount, and category remain.
7. Press Back or use the system back action: the screen calls `onBack` without saving.

### Checks

`./gradlew :app:testDebugUnitTest :app:assembleDebug :app:lintDebug`
