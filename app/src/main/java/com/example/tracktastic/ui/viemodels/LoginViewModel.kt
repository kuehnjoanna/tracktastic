package com.example.tracktastic.ui.viemodels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tracktastic.data.Repository
import com.example.tracktastic.data.model.ActivitiesStatistic
import com.example.tracktastic.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.util.Calendar

class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _currentUser = MutableLiveData<FirebaseUser?>(auth.currentUser)
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser

    //Firebase Dienst Instanzen laden
    val firestoreDatabase = FirebaseFirestore.getInstance()
    val usersCollectionReference = firestoreDatabase.collection("users")
    var userDataDocumentReference: DocumentReference? = null


    // everything repository
    val repository = Repository
    var _info = MutableLiveData<String?>(null)
    val info: LiveData<String?>
        get() = _info

    init {
        setUserEnvironment()
    }

    fun setProfile(profile: User) {
        if (userDataDocumentReference == null) {
            //Funktion abbrechen
            return
        }
        userDataDocumentReference!!.set(profile)
        val name = Calendar.getInstance()
            .get(android.icu.util.Calendar.YEAR).toString() + "." + (Calendar.getInstance()
            .get(android.icu.util.Calendar.MONTH) + 1).toString()
        Log.d("name", name)
        userDataDocumentReference!!.collection("statistics").document(name)
            .set(ActivitiesStatistic())

    }

    fun setUserEnvironment() {
        val user = auth.currentUser
        _currentUser.postValue(user)
        if (user != null) {
            //Immer wenn der User eingeloggt ist muss diese Variable definiert sein
            userDataDocumentReference = usersCollectionReference.document(user.uid)
        } else {
            // userDataDocumentReference!!.set(user)
        }
    }

    fun loadWallpaper() {
        viewModelScope.launch {

            Repository.loadDefaultWallpaper()


        }
    }

    fun loadAvatar(name: String) {
        viewModelScope.launch {

            Repository.loadDefaultAvatar(name)


        }
    }

    fun isUserVerified(): Boolean {
        if (auth.currentUser != null) {

            if (auth.currentUser!!.isEmailVerified) {
                return true
            } else {
                _info.value = "Please Verify your email first"
                return false
            }
        }
        return false

    }

    fun signUpWithFirebase(email: String, password: String, name: String) {
        //funktion die in firebase account macht

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(ContentValues.TAG, "createUserWithEmail:success")
                        //anlegen user daten in firestore
                        val user = User(userName = name, userEmail = email)
                        setUserEnvironment()
                        setProfile(user)
                        loadWallpaper()
                        loadAvatar(name)
                        _info.value = "Please verify your email"
                    }

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