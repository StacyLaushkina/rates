package ru.laushkina.rates.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

class RateViewModel(@DrawableRes val imageId: Int,
                    @StringRes val name: Int,
                    val shortName: String,
                    val amount: Float,
                    // If true, show real amount. If false, show null
                    val showAmount: Boolean)