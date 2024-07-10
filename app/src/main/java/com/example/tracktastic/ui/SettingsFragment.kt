package com.example.tracktastic.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.tracktastic.R
import com.example.tracktastic.databinding.FragmentSettingsBinding
import com.example.tracktastic.ui.viemodels.SettingsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase


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
        //viewModel.loadWallpaper()
        viewModel.retrieveDataFromDatabase()

        viewModel.firebaseWallpaperUrl.observe(viewLifecycleOwner) {
            binding.ivWallpaper.load(viewModel.firebaseWallpaperUrl.value)
            Log.d("firebasewallpa", viewModel.firebaseWallpaperUrl.value!!)

        }
        viewModel.firebaseAvatarUrl.observe(viewLifecycleOwner){
            binding.ivAvatar.load(viewModel.firebaseAvatarUrl.value)
        }

        binding.Name.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }}
