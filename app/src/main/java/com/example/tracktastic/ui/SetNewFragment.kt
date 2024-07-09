package com.example.tracktastic.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tracktastic.R
import com.example.tracktastic.data.model.ClickerActivity
import com.example.tracktastic.databinding.FragmentSetNewBinding
import com.example.tracktastic.ui.viemodels.SettingsViewModel
import com.example.tracktastic.utils.Calculations
import com.example.tracktastic.utils.NavHelper

class SetNewFragment : Fragment(R.layout.fragment_set_new), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentSetNewBinding
    private val SettingViewModel: SettingsViewModel by activityViewModels()
    var day = Calculations.day
    var month = Calculations.month
    var year = Calculations.year
    var hour = Calculations.hour
    var minute = Calculations.minute

    var cleanDate = ""
    var cleanTime = ""
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
        pickTimeAndDate()
        binding.BTNcreateNewActivity.setOnClickListener {
            val size = SettingViewModel.repository.clicketestlist.value!!.size + 1
            val name = binding.ETactivityNameNew.text.toString()
            val clicker = ClickerActivity(size, name)
            if (!name.isNullOrEmpty()){
                SettingViewModel.addNewClicker(size, name)
                findNavController().navigate(SetNewFragmentDirections.actionSetNewFragmentToHomeFragment())
                NavHelper.isHome = true
            }
        }

    }
    private fun pickTimeAndDate(){
        binding.BTNpickDate.setOnClickListener {
            getDateCalendar()
            Log.d("gettime", Calculations.hour.toString())
            DatePickerDialog(requireContext(), this, year, month, day ).show()
        }
        binding.BTNpickTime.setOnClickListener {
            getTimeCalendar()
            TimePickerDialog(context, this, hour, minute, true).show()
        }
        Log.d("current date", Calculations.cleanDate(Calendar.getInstance().get(Calendar.DAY_OF_MONTH),Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.YEAR)))
        Log.d("current date", Calculations.getCurrentDate())
        Log.d("current date", Calculations.getCurrentTime())
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        cleanTime = Calculations.cleanTime(hourOfDay, minute)
        binding.tvTimeSelected.text = "Time: $cleanTime"
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
         cleanDate = Calculations.cleanDate(dayOfMonth, month, year)
        binding.tvDateSelected.text = "Date: $cleanDate"
    }

    private fun getTimeCalendar(){
        val cal = Calendar.getInstance()
         hour = cal.get(Calendar.HOUR_OF_DAY)
         minute = cal.get(Calendar.MINUTE)
    }

    private fun getDateCalendar(){
        val cal = Calendar.getInstance()
         day = cal.get(Calendar.DAY_OF_MONTH)
         month = cal.get(Calendar.MONTH)
         year = cal.get(Calendar.YEAR)
    }


}