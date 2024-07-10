package com.example.tracktastic.ui.viemodels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tracktastic.data.Repository
import com.example.tracktastic.data.model.ClickerActivity
import com.example.tracktastic.data.model.User
import com.example.tracktastic.utils.Calculations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {

    // everything repository
    val repository = Repository

    //data that comes from retriveDataFromFirebase function
    private val _firebaseWallpaperUrl = MutableLiveData<String>()
    val firebaseWallpaperUrl: LiveData<String>
        get() = _firebaseWallpaperUrl


    //data that comes from retriveDataFromFirebase function
    private val _firebaseAvatarUrl = MutableLiveData<String>()
    val firebaseAvatarUrl: LiveData<String>
        get() = _firebaseAvatarUrl
    //data that comes from retriveDataFromFirebase function
    private val _firebaseName = MutableLiveData<String>()
    val firebaseName: LiveData<String>
        get() = _firebaseName

        //firebase firestore

    val firestore = FirebaseFirestore.getInstance()
    val usersCollectionReference = firestore.collection("Users")
    var userDataDocumentReference: DocumentReference? = null

    val firestoreReference = FirebaseFirestore.getInstance().collection("users")
        .document(FirebaseAuth.getInstance().currentUser?.uid.toString())



    //testing evor database change

  val selectedItem = MutableLiveData<ClickerActivity>()
    fun selectedActivityItem(it: ClickerActivity) {
        selectedItem.value = it
    }
    fun timesClicked(name:String, timesClicked: Int) {
        firestoreReference.collection("activities").document(name).update("timesClicked", timesClicked)
    }
    fun updateClickerName(clicker: ClickerActivity, newName: String){
        firestoreReference.collection("activities").document(clicker.id.toString()).update(clicker.name, newName)

    }
    fun lastClicked(id: Int){

        firestoreReference.collection("activities").document(id.toString()).update("lastClickedAt",  Calculations.getCurrentDate())
    }
    fun plusClicked(clicker: ClickerActivity){
        clicker.value = clicker.value + clicker.increment

        firestoreReference.collection("activities").document(clicker.id.toString()).update("value", clicker.value)
    }
    fun minusClicked(clicker: ClickerActivity){
        clicker.timesClicked = clicker.timesClicked - clicker.decrement

        firestoreReference.collection("activities").document(clicker.id.toString()).update("value", clicker.value)
    }
    fun updateClickerDecrement(clicker: ClickerActivity, decrement: Int){
        clicker.decrement = decrement

        firestoreReference.collection("activities").document(clicker.id.toString()).update("decrement", clicker.decrement)
    }
    fun updateClickerIncrement(clicker: ClickerActivity, increment: Int){
        clicker.increment = increment

        firestoreReference.collection("activities").document(clicker.id.toString()).update("increment", clicker.increment)
    }
    fun updateClickerValue(clicker: ClickerActivity, value: Int){
        clicker.value = value

        firestoreReference.collection("activities").document(clicker.id.toString()).update("value", clicker.value)
    }


    fun addNewClicker(size: Int, name: String) {
        val clicker = ClickerActivity(size, name)
        firestoreReference.collection("activities").document(clicker.id.toString()).set(clicker)


    }

    fun removeClicker(name: String) {
        firestoreReference.collection("activities").document(name).delete()
        Log.d("delete", "userId: ${repository.clicketestlist.value}")
    }


    //sollte ich eine user instance machen?


    fun retrieveListFromFirestore() {

        firestoreReference.collection("activities").addSnapshotListener { value, error ->


            Log.d("wtf", "userId: works")
            if (error != null) {
                Log.w("wtf2", "Listen failed.", error)
                return@addSnapshotListener
            }
            if (value != null) {
                val newlist = mutableListOf<ClickerActivity>()
                for (eachactivity in value!!) {
                    Log.d("eachactivity to object",eachactivity.toString())
                    val activity = eachactivity.toObject(ClickerActivity::class.java)
                    if (activity != null) {
                        newlist.add(activity)

                        Log.d("wtf", "userId: ${activity.timesClicked}")
                        Log.d(TAG, "userId: ${activity.name}\"")
                        Log.d(TAG, "userId: ${repository.clicketestlist}")
                    }

                }
                repository._clickertestllist.postValue(newlist)
            } else {
                Log.d(TAG, "Current data: null")
              //  val activity = ClickerActivity() // zum evtl default acctivity zu geben
            }

            Log.d(TAG, "userId: ${repository.clicketestlist}")
        }
    }

        fun retrieveDataFromDatabase() {

            firestoreReference.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: ${snapshot.data}")
                    val user = snapshot.toObject(User::class.java)
                    _firebaseWallpaperUrl.value = user!!.wallpaperUrl
                    _firebaseAvatarUrl.value = user.avatarUrl
                    _firebaseName.value = user.userName


                } else {
                    Log.d(TAG, "Current data: null")
                }
            }

        }
    }