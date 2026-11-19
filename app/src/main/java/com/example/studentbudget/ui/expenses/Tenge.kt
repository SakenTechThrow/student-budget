package com.example.studentbudget.ui.expenses

import java.util.Locale

internal fun formatTenge(amount: Long): String =
    "%,d".format(Locale.ROOT, amount).replace(',', ' ') + " ₸"
