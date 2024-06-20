package com.example.tracktastic.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tracktastic.data.remote.AvatarApi

class Repository {
    private val _avatarBoy = MutableLiveData<Any>()
    val avatarBoy: LiveData<Any>
        get() = _avatarBoy

    private val _avatarGirl = MutableLiveData<String>()
    val avatarGirl: LiveData<String>
        get() = _avatarGirl

    //boolean that is supposed to work according to internet connection
    var _isWorking = MutableLiveData<Boolean>(true)
    val isWorking: LiveData<Boolean>
        get() = _isWorking


    suspend fun loadBoyAvatar() {
        try {
            //getting all news from web
            val response = AvatarApi.apiService.getBoyAvatar()
            Log.d("ApiResponse", response.toString())
            _isWorking.postValue(true)
            //
            _avatarBoy.postValue(response.toString())
        }catch (e: Exception){
            Log.d("ApiResponse", "${e.message}")
            _isWorking.postValue(false)
        }
    }
    suspend fun loadGirlAvatar() {
        try {
            //getting all news from web
            val response = AvatarApi.apiService.getGirlAvatar()
            Log.d("ApiResponse", response.toString())
            _isWorking.postValue(true)
            //
            _avatarGirl.postValue(response.toString())
        }catch (e: Exception){
            Log.d("ApiResponse", "${e.message}")
            _isWorking.postValue(false)
        }
    }
}