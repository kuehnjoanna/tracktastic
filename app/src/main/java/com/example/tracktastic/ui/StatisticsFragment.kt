package com.example.tracktastic.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import com.example.tracktastic.R
import com.example.tracktastic.databinding.FragmentStatisticsBinding
import com.example.tracktastic.ui.viemodels.SettingsViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry


class StatisticsFragment : Fragment() {
    private lateinit var binding: FragmentStatisticsBinding
    private val SettingsViewModel: SettingsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //loading firebase wallpaper
        SettingsViewModel.usEr.observe(viewLifecycleOwner) {
            binding.homelayout.load(it.wallpaperUrl)
        }

        //statistics
        var activityList = SettingsViewModel.repository.clicketestlist.value!!.toList()
        val valueChart = arrayListOf<BarEntry>()
        val minutesSpentChart = arrayListOf<BarEntry>()
        var i = 0f
        var value: Float
        var timeValue: Float

        for (activity in activityList) {
            //checking if values are negative and solving that, then adding to array
            if ((activity.value).toFloat() < 0) {
                value = -(activity.value.toFloat())
                valueChart.add(BarEntry(i, value))
                Log.d("timesclickedChart", BarEntry(i, value).toString())
            } else {
                value = ((activity.value).toFloat())
                valueChart.add(BarEntry(i, value))
                Log.d("timesclickedChart", BarEntry(i, value).toString())
            }
            if (activity.timeSpent.toFloat() < 0) {
                timeValue = -(activity.timeSpent.toFloat())
                minutesSpentChart.add(BarEntry(i, timeValue))
                Log.d("minutesSpentChart", BarEntry(i, timeValue).toString())
            } else {
                timeValue = (activity.timeSpent.toFloat())
                minutesSpentChart.add(BarEntry(i, timeValue))
                Log.d("minutesSpentChart", BarEntry(i, timeValue).toString())
            }

            i = i + 1f
        }
        Log.d("timesClickedChart", valueChart.toString())
        Log.d("minutesSpentChart", minutesSpentChart.toString())

        //setting and customizing Data
        val barDataSetClicks = BarDataSet(valueChart, "Value")
        val barDataSetMinutes = BarDataSet(minutesSpentChart, "Minutes")
        barDataSetClicks.color = ContextCompat.getColor(requireContext(), R.color.primary)
        barDataSetMinutes.color = ContextCompat.getColor(requireContext(), R.color.lavender)
        barDataSetClicks.valueTextColor = ContextCompat.getColor(requireContext(), R.color.primary)
        barDataSetMinutes.valueTextColor =
            ContextCompat.getColor(requireContext(), R.color.lavender)
        barDataSetClicks.valueTextSize = 20f
        barDataSetMinutes.valueTextSize = 20f
        barDataSetClicks.label = "Times clicked"
        barDataSetMinutes.label = "Minutes"


        val barData = BarData(barDataSetClicks)
        barData.addDataSet(barDataSetMinutes)

        //customizing bar chart view
        binding.horizontalBarChart.animateY(1500)
        binding.horizontalBarChart.setFitBars(true)
        binding.horizontalBarChart.data = barData
        binding.horizontalBarChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.horizontalBarChart.legend.isEnabled = true
        binding.horizontalBarChart.legend.textColor = Color.WHITE


    }


}