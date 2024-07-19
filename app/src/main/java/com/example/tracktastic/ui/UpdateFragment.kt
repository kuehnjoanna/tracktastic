package com.example.tracktastic.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tracktastic.databinding.FragmentUpdateBinding
import com.example.tracktastic.ui.viemodels.SettingsViewModel

class UpdateFragment : Fragment() {
    private lateinit var binding: FragmentUpdateBinding

    private val SettingViewModel: SettingsViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        SettingViewModel.selectedItem.observe(viewLifecycleOwner) {
            binding.etNewActivityName.hint = it.name
            binding.etIncrement.hint = it.increment.toString()
            binding.etDecrement.hint = it.decrement.toString()
            binding.etValue.hint = it.value.toString()
            if (it.reminders == true) {
                !binding.reminderSwitch.isChecked
            } else {
                binding.reminderSwitch.isChecked
            }
            binding.BTNcreateNewActivity.setOnClickListener {
                val size = SettingViewModel.repository.clicketestlist.value!!.size + 1
                var name = binding.etNewActivityName.text.toString()
                var increment = 1//binding.etIncrement.text.toString().toInt()
                if (!binding.etIncrement.text.isNullOrEmpty()) {
                    increment = binding.etIncrement.text.toString().toInt()

                    SettingViewModel.updateClicker("increment", increment)
                }
                var decrement = 1//binding.etDecrement.text.toString().toInt()
                if (!binding.etDecrement.text.isNullOrEmpty()) {
                    decrement = binding.etDecrement.text.toString().toInt()

                    SettingViewModel.updateClicker("decrement", decrement)
                }
                var value = 0//binding.etValue.text.toString().toInt()
                if (!binding.etValue.text.isNullOrEmpty()) {
                    value = binding.etValue.text.toString().toInt()

                    SettingViewModel.updateClicker("value", value)
                }
                Log.d("what?", "$increment $decrement $value")
                if (!name.isNullOrEmpty()) {

                    SettingViewModel.updateClicker("name", name)
                }
                findNavController().navigate(SetNewFragmentDirections.actionSetNewFragmentToHomeFragment())
                // SettingViewModel.selectedItem.value = null


            }

        }
    }
}