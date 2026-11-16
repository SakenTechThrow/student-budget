# Student Budget

Student Budget is a Kotlin and Jetpack Compose app for tracking student expenses in whole Kazakhstani tenge.

## Team

| Part | Owner |
| --- | --- |
| Android setup and expense list | Saken (`@SakenTechThrow`) |
| Expense data and ViewModel | Add name |
| Add Expense form | Add name |
| Expense Details and navigation | Aidyn Kerembek |

## Week 12 features

- The expense list shows the total, reusable expense cards, an empty state, and an Add expense action. The demo starts at 920 ₸ (Bus and Coffee).
- The Add expense form validates the title, positive whole-tenge amount, and category. Save returns to the list once the shared ViewModel accepts the entry. The form keeps entered values after a failed save and across rotation. Back returns without saving.
- Tapping a card opens details for that expense ID. Details shows the title, category, and formatted amount. Back returns to the previous list.
- Expense data is held in memory. Restarting the app process restores the demo entries.

## Run locally

1. Open this repository in Android Studio. Install Android SDK 37 and use a JDK compatible with the included Gradle 9.6 wrapper (for example, JDK 17).
2. Sync the project and run the `app` configuration on an Android 7.0 (API 24) or newer device or emulator.
3. To build at the command line, use `./gradlew :app:assembleDebug` (`gradlew.bat :app:assembleDebug` on Windows).

## How the screens connect

`MainActivity` owns one `BudgetViewModel`. `BudgetNavHost` observes its expense list and total, passes valid form input to `addExpense`, and looks up the selected expense by ID for the details screen. See [the data layer notes](PART2_INTEGRATION.md) for the repository and ViewModel API.

## Verify navigation

1. Launch the app and confirm that the total is 920 ₸. Open **Bus** and verify **Transport** and **120 ₸**. Tap **Back**.
2. Open **Add expense**. Enter **Lunch**, **0**, and **Food**, then tap **Save**. The amount error should appear and the form should remain open.
3. Change the amount to **1500** and tap **Save**. Confirm that the list shows **Lunch** and a total of **2 420 ₸**.
4. Open **Lunch** and verify **Food** and **1 500 ₸**. Use Android's system Back and confirm the same list and total are still displayed.

Run the unit tests, build, and lint with `./gradlew :app:testDebugUnitTest :app:assembleDebug :app:lintDebug`.
