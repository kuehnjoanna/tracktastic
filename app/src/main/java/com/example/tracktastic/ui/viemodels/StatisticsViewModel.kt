package com.example.tracktastic.ui.viemodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StatisticsViewModel : ViewModel() {
    var name = java.util.Calendar.getInstance()
        .get(android.icu.util.Calendar.YEAR).toString() + "." + (java.util.Calendar.getInstance()
        .get(android.icu.util.Calendar.MONTH) + 1).toString()

    val reference = FirebaseFirestore.getInstance().collection("users")
        .document(FirebaseAuth.getInstance().currentUser?.uid.toString()).collection("statistics")
    val reference2 = FirebaseFirestore.getInstance().collection("users")
        .document(FirebaseAuth.getInstance().currentUser?.uid.toString()).collection("statistics")
        .document("name")

    val stats: MutableMap<Int, MutableList<Int>> = mutableMapOf()

    fun addValueToStatsMap(key: Int, value: Int) {
        reference2.update("key", value)
        if (stats.containsKey(key)) {
            // If the key exists, add the value to the existing list
            stats[key]?.add(value)
        } else {
            // If the key does not exist, create a new list and add the value
            stats[key] = mutableListOf(value)
        }
    }
}