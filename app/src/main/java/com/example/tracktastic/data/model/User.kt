package com.example.tracktastic.data.model

import com.example.tracktastic.ui.viemodels.SettingsViewModel

data class User(
    val wallpaperUrl: String = "",//"${SettingsViewModel().firebaseWallpaperUrl.value.toString()}",
    val avatarUrl: String = "${SettingsViewModel().firebaseAvatarUrl.value.toString()}",
    val userName: String = "",
    val userEmail: String = ""


)