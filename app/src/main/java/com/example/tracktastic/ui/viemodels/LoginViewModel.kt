package com.example.tracktastic.ui.viemodels

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.tracktastic.data.Repository
import com.example.tracktastic.ui.login.ForgotPasswordFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.UUID

class LoginViewModel : ViewModel() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val reference: DatabaseReference = database.reference.child("Users")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var _backgroundPictureUrl = ""
    val backgroundPictureUrl: String
        get() = _backgroundPictureUrl

    val firebaseReference =
        FirebaseDatabase.getInstance().reference.child("Users")
    //var user = auth.currentUser
    private val _currentUser = MutableLiveData<FirebaseUser?>(auth.currentUser)
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser

    // everything repository
    val repository = Repository
    var _info = MutableLiveData<String?>(null)
    val info: LiveData<String?>
        get() = _info

    init {
        setUserEnvironment()
    }

    fun setUserEnvironment() {
        val user = auth.currentUser
        _currentUser.postValue(user)

    }

    fun loadWallpaper() {
        viewModelScope.launch {

            Repository.loadDefaultWallpaper()


        }
    }

    fun signUpWithFirebase(email: String, password: String, name: String) {
        //funktion die in firebase account macht

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    //anlegen user daten in realtime datacase

                     firebaseReference.child(FirebaseAuth.getInstance().currentUser?.uid.toString()).child("userName").setValue(name)
                     firebaseReference.child(FirebaseAuth.getInstance().currentUser?.uid.toString()).child("userEmail").setValue(email)


                    setUserEnvironment()
                } else {
                    _info.value = "${task.exception!!.message}"

                    // If sign in fails, display a message to the user.
                    Log.w(
                        "viemodel",
                        "createUserWithEmail:failure, ${auth.currentUser},${task.exception!!.message}",
                        task.exception
                    )

                }

            }

    }

    fun logOut() {
        auth.signOut()
        setUserEnvironment()
    }

    //12 characters at least one digit and at least one upper case
    fun isValidPassword(password: String): Boolean {
        val passwordPattern = """^\S{6,}$"""
        val passwordRegex = Regex(passwordPattern)
        return passwordRegex.matches(password)
    }

    fun passwordReset(email: String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                Log.d(ContentValues.TAG, "resetting password:success")
            } else {
                Log.d(ContentValues.TAG, "resetting password:failure")
            }
        }
    }

    fun signInWithFirebase(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithEmail:success, ${auth.currentUser}")
                    //   _loginSuccess.value = true
                    setUserEnvironment()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                    _info.value = task.exception!!.message
                    //  _loginSuccess.value = false

                }
            }
    }
}