package com.example.tracktastic.ui.viemodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tracktastic.data.Repository
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {
    val repository = Repository()
   val avatar = repository.avatarBoy
    fun loadBoyAvatar(){
        viewModelScope.launch {
            repository.loadBoyAvatar()
        }
    }
}