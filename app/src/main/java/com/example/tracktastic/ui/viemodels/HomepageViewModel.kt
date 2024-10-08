package com.example.tracktastic.ui.viemodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tracktastic.data.model.ClickerActivity
import com.example.tracktastic.ui.DialogsAndToasts
import com.example.tracktastic.utils.Timer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomepageViewModel : ViewModel() {
    val timerLogic = Timer()
    val dialogsToasts = DialogsAndToasts

    val selectedItem = MutableLiveData<ClickerActivity>()

    var isSelectedItemClicked = false
    fun selectedActivityItem(it: ClickerActivity) {
        selectedItem.value = it
        isSelectedItemClicked = true
    }

    fun addDuration(time: Int) {
        //  val updatedDuration = selectedItem.value!!.timeSpent + timerLogic.duration2.value!!
        val updatedDuration = selectedItem.value!!.timeSpent + time
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .collection("activities").document(selectedItem.value!!.id.toString())
            .update("timeSpent", updatedDuration)

    }
}