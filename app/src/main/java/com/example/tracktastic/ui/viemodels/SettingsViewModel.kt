package com.example.tracktastic.ui.viemodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tracktastic.data.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class SettingsViewModel : ViewModel() {

    //everything Firebase
    val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    val storageRefrence: StorageReference = firebaseStorage.reference
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val reference: DatabaseReference = database.reference.child("Users").child(FirebaseAuth.getInstance().currentUser?.uid.toString())
    val firebasePic: DatabaseReference =
        reference.child("wallpaperUrl") // from here i need to get value from firebase and figureouthowto

    // everything repository
    val repository = Repository

    //data that comes from retriveDataFromFirebase function
    private val _firebaseWallpaperUrl = MutableLiveData<String>()
    val firebaseWallpaperUrl: LiveData<String>
        get() = _firebaseWallpaperUrl

//sollte ich eine user instance machen?
    fun retrieveDataFromDatabase() {
        firebasePic.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                val user = snapshot.getValue(String::class.java)
                if (user != null) {

                    println("wallpaperUrl: ${user}")

                    _firebaseWallpaperUrl.value = user.toString()
                    Log.d("wallpaperUrlLiveData", _firebaseWallpaperUrl.value.toString())

                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
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