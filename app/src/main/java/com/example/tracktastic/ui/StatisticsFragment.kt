package com.example.tracktastic.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.tracktastic.databinding.FragmentStatisticsBinding
import com.example.tracktastic.ui.viemodels.SettingsViewModel
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


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
        var list = mutableListOf<Pair<String, Float>>()
        var list2 = mutableListOf<Pair<String, Float>>()
        val entries = ArrayList<Entry>()
        val entries2 = ArrayList<PieEntry>()
        var index = 0
        for (activity in SettingsViewModel.repository.clicketestlist.value!!) {
            list.add(index, activity.name to activity.timeSpent.toFloat())
            list2.add(index, activity.name to (activity.timesClicked.toFloat() * 10))
            entries.add(Entry(index.toFloat(), activity.timesClicked.toFloat()))
            entries2.add(PieEntry(activity.timesClicked.toFloat(), activity.name))
            index++

        }
        Log.d("list1", list.toString())
        Log.d("list2", list2.toString())

        val dataSet2 = PieDataSet(entries2, "Sample Data")
        dataSet2.colors = listOf(Color.RED, Color.GREEN, Color.BLUE)
        dataSet2.valueTextColor = Color.BLACK
        dataSet2.valueTextSize = 16f

        val pieData = PieData(dataSet2)
        binding.pieChart.data = pieData
        val dataSet = LineDataSet(entries, "Sample Data")
        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.BLACK

        val lineData = LineData(dataSet)
        binding.anotherchart.data = lineData

        // Customize chart appearance
        binding.anotherchart.description = Description().apply { text = "Sample Line Chart" }
        binding.anotherchart.animateY(1000)


    }
    /** Setup data for horizontal chart */

}