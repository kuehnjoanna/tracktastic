package com.example.tracktastic.data.model

import android.icu.util.Calendar

data class ActivitiesStatistic(
    val year: Int = Calendar.getInstance().get(Calendar.YEAR),
    val month: Int = Calendar.getInstance().get(Calendar.MONTH) + 1,

    )