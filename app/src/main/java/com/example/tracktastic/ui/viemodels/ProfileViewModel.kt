package com.example.tracktastic.ui.viemodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tracktastic.data.Repository
import com.example.tracktastic.data.model.AvatarResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {
    val repository = Repository()
   val avatar = repository.avatarBoy
    fun loadBoyAvatar(){     CoroutineScope(Dispatchers.Main).launch {
        val avatarBitmap = repository.loadBoyAvatar()
        if (avatarBitmap != null) {
            Log.d("fetch", "works")
        } else {
            Log.d("fetch", "no works")
        }

    }
    }
}