package com.example.tracktastic.ui.viemodels

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.tracktastic.ui.login.ForgotPasswordFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginViewModel : ViewModel() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val reference: DatabaseReference = database.reference.child("Users")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    //var user = auth.currentUser
    private val _currentUser = MutableLiveData<FirebaseUser?>(auth.currentUser)
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser



   // var loginSuccess = false
    /*

    var _loginSuccess = MutableLiveData<Boolean?>(false)
    val loginSuccess: LiveData<Boolean?>
        get() = _loginSuccess

     */
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
    fun signUpWithFirebase(email: String, password: String, name: String) {
        //funktion die in firebase account macht

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(ContentValues.TAG, "createUserWithEmail:success")
                        //anlegen user daten in realtime datacase
                        /*
                        val userID = auth.currentUser!!.uid
                        reference.child("ID").child("$userID")
                        reference.child("ID").child(userID).child("Name").setValue(name)
                        reference.child("ID").child(userID).child("Email").setValue(email)

                         */
                       setUserEnvironment()
                    } else {
                        _info.value = "${task.exception!!.message}"
                       // _loginSuccess.value = false
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
    fun isValidEmail(email: String): Boolean {
        //lowercase, number or _-/+@/same/ + . / same
        val emailPattern = """^[a-z0-9_-]+@[a-z0-9-_]+\.[a-z0-9-_]{2,}$"""
        val emailRegex = Regex(emailPattern)
        return emailRegex.matches(email)
    }
fun passwordReset(email:String){
    auth.sendPasswordResetEmail(email).addOnCompleteListener { task->
        if (task.isSuccessful){

            Log.d(ContentValues.TAG, "resetting password:success")
        }else{
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