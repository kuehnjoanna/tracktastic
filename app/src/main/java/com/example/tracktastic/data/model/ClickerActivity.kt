package com.example.tracktastic.data.model

data class ClickerActivity (
    val name: String = "",
    var timesClicked: Int = 0,
    var increment: Int = 0,
    var decrement: Int = 0,
    var value: Int = 0,
)