package com.example.tracktastic.ui.viemodels

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.tracktastic.data.Repository
import com.example.tracktastic.data.model.ClickerActivity
import com.example.tracktastic.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class SettingsViewModel : ViewModel() {

    // everything repository
    val repository = Repository

    //data that comes from retriveDataFromFirebase function
    private val _firebaseWallpaperUrl = MutableLiveData<String>()
    val firebaseWallpaperUrl: LiveData<String>
        get() = _firebaseWallpaperUrl

        //firebase firestore

    val firestore = FirebaseFirestore.getInstance()
    val usersCollectionReference = firestore.collection("Users")
    var userDataDocumentReference: DocumentReference? = null

    val firestoreReference = FirebaseFirestore.getInstance().collection("users")
        .document(FirebaseAuth.getInstance().currentUser?.uid.toString())




    //testing evor database change

   /* val selectedArticle = MutableLiveData<ClickerActivity>()
    fun selectedActivityItem(it: ClickerActivity) {
        selectedArticle.value = it
    }

    */

    fun addNewClicker(name: String) {
        val newClicker = ClickerActivity(name)
        firestoreReference.collection("activities").document(name).set(newClicker)


    }

    fun removeClicker(name: String) {
        firestoreReference.collection("activities").document(name).delete()
        Log.d("delete", "userId: ${repository.clicketestlist.value}")
    }

    //sollte ich eine user instance machen?
    fun retrieveAcitivitiesFromDatabase() {

        firestoreReference.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w(TAG, "Listen failed.", error)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: ${snapshot.data}")
                val user = snapshot.toObject(User::class.java)
                _firebaseWallpaperUrl.value = user!!.wallpaperUrl

            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }

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

                } else {
                    Log.d(TAG, "Current data: null")
                }
            }

        }

        fun loadBoyAvatar() {
            CoroutineScope(Dispatchers.Main).launch {
                val avatarBitmap = repository.loadBoyAvatar()
                if (avatarBitmap != null) {
                    Log.d("fetch", "works")
                } else {
                    Log.d("fetch", "no works")
                }

            }
        }
    }