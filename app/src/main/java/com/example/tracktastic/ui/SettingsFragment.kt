package com.example.tracktastic.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import coil.load
import com.example.tracktastic.R
import com.example.tracktastic.data.Repository
import com.example.tracktastic.data.model.Hit
import com.example.tracktastic.databinding.FragmentSettingsBinding
import com.example.tracktastic.ui.viemodels.SettingsViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  viewModel.loadWallpaper()
        viewModel.retrieveDataFromDatabase()
        viewModel.firebaseWallpaperUrl.observe(viewLifecycleOwner) {
        binding.wallpaperIV.load(viewModel.firebaseWallpaperUrl.value)
            Log.d("firebasewallpa", viewModel.firebaseWallpaperUrl.value!!)
            binding.textView.text = "Settings"
    }
//profilbild
        CoroutineScope(Dispatchers.Main).launch {
            val avatarBitmap = viewModel.repository.loadBoyAvatar()
            if (avatarBitmap != null) {

                Log.d("settings", avatarBitmap.toString())
val bildprofile = "https://avatar.iran.liara.run/public/boy"
val bildprofile2 = "https://avatar.iran.liara.run/public/boy"
              //  binding.ivAvatar.setImageBitmap(avatarBitmap)
                binding.ivAvatar.setImageBitmap(avatarBitmap)
                binding.test.load(bildprofile2)
                binding.test.setOnClickListener{
                    CoroutineScope(Dispatchers.Main).launch {
                        viewModel.repository.upload(bildprofile2, "profilePicUrl") {
                            if (it != null) {
                                Log.d(
                                    "upload function",
                                    "success, ${Repository.uri}"
                                )// diese ist immer noch leer, aber*
                            } else {
                                Log.d("upload function", "fail, ${Repository.uri}")
                            }

                        }
                    }

                }

            } else {

                Log.d("settings", "no works")
            }
        }


        //Log.d("avatar", viewModel.avatar.toString())
    }


}
