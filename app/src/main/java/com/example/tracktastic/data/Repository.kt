package com.example.tracktastic.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tracktastic.data.model.AvatarResponse
import com.example.tracktastic.data.remote.AvatarApi
import com.google.android.gms.common.internal.ImagesContract.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.URL

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


    suspend fun loadBoyAvatar(): Bitmap? {
        return withContext(Dispatchers.IO) {
            val url = URL("https://avatar.iran.liara.run/public/boy")
            val urlConnection = url.openConnection() as HttpURLConnection
            try {
                urlConnection.requestMethod = "GET"
                urlConnection.connect()

                if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = urlConnection.inputStream
                    BitmapFactory.decodeStream(inputStream)
                } else {
                    null
                }
            } finally {
                urlConnection.disconnect()
            }
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