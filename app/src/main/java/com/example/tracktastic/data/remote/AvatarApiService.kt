package com.example.tracktastic.data.remote

import com.example.tracktastic.data.model.AvatarResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


//const val API_KEY = "34131f3f211b4ca69a5dcc638bb2997d"
const val BASE_URL = "https://avatar.iran.liara.run/"

val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(
        BASE_URL
    ).build()

interface AvatarApiService {

    @GET("public/boy")
    suspend fun getBoyAvatar(): Call<AvatarResponse>

    @GET("public/girl")
    suspend fun getGirlAvatar(): AvatarResponse

}

object AvatarApi {
    val apiService: AvatarApiService by lazy { retrofit.create(AvatarApiService::class.java) }
}
