package com.example.tracktastic.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tracktastic.R
import com.example.tracktastic.databinding.FragmentHomeBinding
import com.example.tracktastic.databinding.FragmentSetNewBinding
import com.example.tracktastic.ui.viemodels.LoginViewModel
import com.example.tracktastic.ui.viemodels.SettingsViewModel

class SetNewFragment : Fragment() {

    private lateinit var binding: FragmentSetNewBinding
    private val viewModel: SettingsViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSetNewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.BTNcreateNewActivity.setOnClickListener {
            val name = binding.ETactivityNameNew.text.toString()
            if (!name.isNullOrEmpty()){
                viewModel.addNewClicker(name)
                findNavController().navigate(SetNewFragmentDirections.actionSetNewFragmentToHomeFragment())
            }
        }
    }
}