package com.example.tracktastic.data.model

import com.example.tracktastic.utils.Calculations

data class ClickerActivity (
    val id: Int = 0,
    var name: String = "",
    var timesClicked: Int = 0,
    var increment: Int = 1,
    var decrement: Int = 1,
    var value: Int = 0,
    var createdAt: String = "Created on: ${Calculations.getCurrentDate()} \n at: ${Calculations.getCurrentTime()}",
    var lastClickedAt: String = createdAt
)