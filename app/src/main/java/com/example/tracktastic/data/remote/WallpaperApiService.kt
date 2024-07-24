package com.example.tracktastic.data.remote

import com.example.tracktastic.data.model.WallpaperResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

const val BASE_URL = "https://pixabay.com/api/"

val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(
        BASE_URL
    ).build()

interface WallpaperApiService {

    @GET("?key=44482891-25a4348e5d04e7daf44644db6&id=967967")
    suspend fun getDefaultWallpaper(): WallpaperResponse

    @GET("?key=44482891-25a4348e5d04e7daf44644db6&id=8727795")
    suspend fun getWaterWallpaper(): WallpaperResponse

    @GET("?key=44482891-25a4348e5d04e7daf44644db6&id=8653526")
    suspend fun getPlantWallpaper(): WallpaperResponse

}

object WallpaperApi {
    val apiService: WallpaperApiService by lazy { retrofit.create(WallpaperApiService::class.java) }
}
