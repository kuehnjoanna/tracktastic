package com.example.tracktastic.data.remote

import com.example.tracktastic.data.model.WallpaperResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

//https://pixabay.com/photos/milky-way-night-sea-landscape-967967/
//const val API_KEY = "34131f3f211b4ca69a5dcc638bb2997d"
const val BASE_URL = "https://pixabay.com/api/"   //"https://avatar.iran.liara.run/"

val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(
        BASE_URL
    ).build()
//https://pixabay.com/photos/blue-feather-bird-macaw-background-6959585/
//https://pixabay.com/images/search/painted%20picture%20color%20phone%20wallpaper/?orientation=vertical&colors=blue

interface WallpaperApiService {

    @GET("?key=44482891-25a4348e5d04e7daf44644db6&id=967967")
    suspend fun getDefaultWallpaper(): WallpaperResponse

    @GET("?key=44482891-25a4348e5d04e7daf44644db6&id=6959585")
    suspend fun getFeatherWallpaper(): WallpaperResponse

}

object WallpaperApi {
    val apiService: WallpaperApiService by lazy { retrofit.create(WallpaperApiService::class.java) }
}
