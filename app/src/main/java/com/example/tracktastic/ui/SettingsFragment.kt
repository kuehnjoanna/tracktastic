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
import com.example.tracktastic.databinding.ConfirmDialogBinding
import com.example.tracktastic.databinding.FragmentSettingsBinding
import com.example.tracktastic.ui.viemodels.SettingsViewModel


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val settingsViewModel: SettingsViewModel by activityViewModels()

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
        settingsViewModel.retrieveDataFromDatabase()

        settingsViewModel.firebaseWallpaperUrl.observe(viewLifecycleOwner) {
            //   binding.ivWallpaper.load(viewModel.firebaseWallpaperUrl.value)
            Log.d("firebasewallpa", settingsViewModel.firebaseWallpaperUrl.value!!)
            binding.ivWallpaper.load(R.drawable.testbg)

        }
        settingsViewModel.firebaseAvatarUrl.observe(viewLifecycleOwner) {
            binding.ivAvatar.load(settingsViewModel.firebaseAvatarUrl.value)
        }
        settingsViewModel.currentUser.observe(viewLifecycleOwner) {
            binding.delete.setOnClickListener {
                DialogsAndToasts.addDialog(
                    requireContext(),
                    R.string.dialog_account_delete,
                    ConfirmDialogBinding.inflate(layoutInflater)
                ) {
                    settingsViewModel.deleteAccoount(
                        requireContext(),
                        R.string.toast_account_deleted,
                        R.string.toast_something_went_wrong
                    )
                }
                findNavController().navigate(R.id.loginFragment)
            }
        }


        binding.Name.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToDesignFragment())
        }
    }
}
