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
import com.example.tracktastic.databinding.FragmentSetNewBinding
import com.example.tracktastic.ui.viemodels.LoginViewModel
import com.example.tracktastic.ui.viemodels.SettingsViewModel

class SetNewFragment : Fragment(R.layout.fragment_set_new) {

    private lateinit var binding: FragmentSetNewBinding
    private val SettingViewModel: SettingsViewModel by activityViewModels()

    private val loginViewModel: LoginViewModel by activityViewModels()

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
        SettingViewModel.usEr.observe(viewLifecycleOwner) {
            binding.homelayout.load(it.wallpaperUrl)
        }


        binding.BTNcreateNewActivity.setOnClickListener {
            val size = SettingViewModel.repository.clicketestlist.value!!.size + 1
            var name = binding.etNewActivityName.text.toString()
            var increment = 1//binding.etIncrement.text.toString().toInt()
            if (!binding.etIncrement.text.isNullOrEmpty()) {
                increment = binding.etIncrement.text.toString().toInt()
            }
            var decrement = 1//binding.etDecrement.text.toString().toInt()
            if (!binding.etDecrement.text.isNullOrEmpty()) {
                decrement = binding.etDecrement.text.toString().toInt()
            }
            var value = 0//binding.etValue.text.toString().toInt()
            if (!binding.etValue.text.isNullOrEmpty()) {
                value = binding.etValue.text.toString().toInt()
            }
            Log.d("what?", "$increment $decrement $value")
            if (!name.isNullOrEmpty()) {


                SettingViewModel.addNewClicker(name, size, increment, decrement, value)
                findNavController().navigate(SetNewFragmentDirections.actionSetNewFragmentToHomeFragment())
              
            }
        }



        binding.reminderSwitch.setOnClickListener {
            if (binding.reminderSwitch.isChecked) {
                DialogsAndToasts.showToast(R.string.notification_off, requireContext())
                loginViewModel.notificationPermission(false)
            } else if (!binding.reminderSwitch.isChecked) {
                DialogsAndToasts.showToast(R.string.notification_on, requireContext())
                loginViewModel.notificationPermission(true)
            }
        }
    }

    /*
      if (Calculations.getCurrentDate() == data.lastClickedAt) {
                    dont send alert, else send alert

     */


}
