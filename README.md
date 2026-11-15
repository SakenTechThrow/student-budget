# Student Budget

Student Budget is a Kotlin and Jetpack Compose app for tracking student expenses in whole Kazakhstani tenge.

## Team

| Part | Owner |
| --- | --- |
| Android setup and expense list | Saken (`@SakenTechThrow`) |
| Expense data and ViewModel | Add name |
| Add Expense form | Add name |
| Expense Details and navigation | Add name |

## Week 12 features

- The expense list shows the total, reusable expense cards, an empty state, and an Add expense action. Its demo starts at 920 ₸ (Bus and Coffee).
- The team will connect the list to shared ViewModel state, add a validated expense form, and navigate to the details screen by expense ID.

## Run locally

1. Open this repository in Android Studio. Install Android SDK 37 and use a JDK compatible with the included Gradle 9.6 wrapper (for example, JDK 17).
2. Sync the project and run the `app` configuration on an Android 7.0 (API 24) or newer device or emulator.
3. To build at the command line, use `./gradlew :app:assembleDebug` (`gradlew.bat :app:assembleDebug` on Windows).

## Integrating the list

Call `ExpenseListScreen` with the current expenses and total from the shared ViewModel. Map each domain expense to an `ExpenseListItem` with an integer ID and amount, and display text for its category. Connect `onExpenseClick(id)` to the details route and `onAddExpense()` to the form route. The starter activity demonstrates these callbacks with snackbars.
