package com.example.tracktastic.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tracktastic.data.model.ClickerActivity
import com.example.tracktastic.data.model.Hit
import com.example.tracktastic.data.remote.WallpaperApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
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

object Repository {
    //testing bevor database chang
    val _clickertestllist = MutableLiveData<List<ClickerActivity>>()
    val clicketestlist: LiveData<List<ClickerActivity>>
        get() = _clickertestllist


    //Api response results from getting wallpapers
    private var _wallpaper1 = MutableLiveData<List<Hit>>()
    val wallpaper1: LiveData<List<Hit>>
        get() = _wallpaper1

    private var _wallpaper2 = MutableLiveData<List<Hit>>()
    val wallpaper2: LiveData<List<Hit>>
        get() = _wallpaper2

    private var _wallpaper3 = MutableLiveData<List<Hit>>()
    val wallpaper3: LiveData<List<Hit>>
        get() = _wallpaper3

    //wallpaper URI
    private var _uri = MutableLiveData<String>()
    val uri: LiveData<String>
        get() = _uri

    //boolean that is supposed to work according to internet connection
    var _isWorking = MutableLiveData<Boolean>(true)
    val isWorking: LiveData<Boolean>
        get() = _isWorking

    //firebase reference um einfacher in den firebase path zu speichern
    val firebaseReference =
        FirebaseDatabase.getInstance().reference.child("Users")

    init {
        //to check how many instances of one repository i made
        Log.d("Repository", "Repository erzeugt")
    }


    suspend fun loadDefaultAvatar(first: String) {
        try {
            //getting Avatar
            Log.d(
                "avatarresponse",
                "https://avatar.iran.liara.run/username?username=$first&length=1"
            )


            //url that is used for downloading and uploading the picture into Firebase with upload function
            val avatarURL = "https://avatar.iran.liara.run/username?username=$first&length=1"
            Log.d("ApiResponseURL", avatarURL)
            //uploading wallpaper
            upload(avatarURL, "avatarUrl") {
                if (it != null) {
                    Log.d("upload function", "success, ${uri}")// diese ist immer noch leer, aber*
                } else {
                    Log.d("upload function", "fail, ${uri}")
                }

            }

        } catch (e: Exception) {
            Log.d("AvatarResponse", "${e.message}")

        }
        // return avatarUrl
    }

    //funktionen um wallpaper vom api zu laden
    suspend fun loadDefaultWallpaper() {
        try {
            //getting WALLPAPER
            val response = WallpaperApi.apiService.getDefaultWallpaper()
            Log.d("ApiResponseAll", response.toString())
            _isWorking.postValue(true)
            _wallpaper3.postValue(response.hits)
            // _response.postValue(response.hits)

            //url that is used for downloading and uploading the picture into Firebase with upload function
            val wallpaperURL = response.hits[0].largeImageURL.toString()
            Log.d("ApiResponseURL", wallpaperURL)
            //uploading wallpaper
            upload(wallpaperURL, "wallpaperUrl") {
                if (it != null) {
                    Log.d("upload function", "success, ${uri}")// diese ist immer noch leer, aber*
                } else {
                    Log.d("upload function", "fail, ${uri}")
                }

            }

        } catch (e: Exception) {
            Log.d("ApiResponse", "${e.message}")
            _isWorking.postValue(false)
        }
        // return wallpaperURL
    }

    suspend fun loadOtherWallpapers() {
        try {
            //getting WALLPAPER
            val response1 = WallpaperApi.apiService.getWaterWallpaper()
            Log.d("ApiWaterResponseAll", response1.toString())
            _isWorking.postValue(true)
            _wallpaper1.postValue(response1.hits)

            val response2 = WallpaperApi.apiService.getPlantWallpaper()
            Log.d("ApiWaterResponseAll", response2.toString())
            _isWorking.postValue(true)
            _wallpaper2.postValue(response2.hits)


            val response = WallpaperApi.apiService.getDefaultWallpaper()
            Log.d("ApiResponseAll", response.toString())
            _isWorking.postValue(true)
            _wallpaper3.postValue(response.hits)
            //url that is used for downloading and uploading the picture into Firebase with upload function
            val wallpaperURL = response2.hits[0].largeImageURL.toString()

            Log.d("ApiFeatherResponseURL", wallpaperURL)
            //uploading wallpaper
            /*
            upload(wallpaperURL, "wallpaperUrl"){
                if (it !=null){
                    Log.d("upload feather function", "success, ${uri}")// diese ist immer noch leer, aber*
                }else{
                    Log.d("upload feather function", "fail, ${uri}")
                }

            }

             */

        } catch (e: Exception) {
            Log.d("ApifeatherResponse", "${e.message}")
            _isWorking.postValue(false)

        }
        // return wallpaperURL


    }

    //funktion die bild ausm firebase storage link zum firebase realtime schickt
    suspend fun upload(string: String, path: String, onComplete: (String?) -> Unit) {
        if (string.isNullOrEmpty()) {
            //checking if parameter from the response is not null or empty
            Log.e("upload", "Invalid URL: $string")
            onComplete(null)
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            //giving user id + wallpaper name to a picture
            val imageName = FirebaseAuth.getInstance().currentUser?.uid.toString() + path
            //uri that is returned from downloading/uploading function
            val imageUri = downloadImageAndUploadToFirebase(string, imageName)
            //saving the uri to firebase reealtime database

            FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
                .update(path, imageUri)

            if (imageUri != null) {
                Log.d("image to bytearray", "success: $imageUri")
                withContext(Dispatchers.IO) {
                    onComplete(imageUri)
                }
            } else {
                Log.d("image to bytearray1", "fail")
            }
        }
    }

    //funktion die url von api ins bytearray umwandelt und zum firebase storage schickt
    private suspend fun downloadImageAndUploadToFirebase(
        imageUrl: String,
        imageName: String
    ): String? {
        return withContext(Dispatchers.IO) {
            //checking if imageurl is null or empty
            val url = try {
                URL(imageUrl)
            } catch (e: MalformedURLException) {
                Log.e("image to bytearray error", "Invalid URL: $imageUrl")
                return@withContext null
            }
            //changing image to bitmap to download it and to byte array to upload it to firebase
            val urlConnection = url.openConnection() as HttpURLConnection
            try {
                urlConnection.requestMethod = "GET"
                urlConnection.connect()

                if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    //Convert to bitmap
                    val inputStream = urlConnection.inputStream
                    val bitmap = BitmapFactory.decodeStream(inputStream)

                    // Convert bitmap to byte array
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                    val data = baos.toByteArray()

                    // Upload to Firebase Storage
                    val storageRef = Firebase.storage.reference.child("images/$imageName.png")
                    storageRef.putBytes(data).await()

                    // Get download URL
                    storageRef.downloadUrl.await().toString()

                } else {
                    null
                }
            } catch (e: Exception) {
                Log.d("image to bytearray error", e.message.toString())
                null
            } finally {
                urlConnection.disconnect()
            }
        }

    }

}