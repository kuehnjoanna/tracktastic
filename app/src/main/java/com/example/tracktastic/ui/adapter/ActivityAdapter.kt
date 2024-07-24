package com.example.tracktastic.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tracktastic.R
import com.example.tracktastic.data.model.ClickerActivity
import com.example.tracktastic.databinding.ClickerItemBinding
import com.example.tracktastic.ui.viemodels.SettingsViewModel
import com.example.tracktastic.utils.Calculations
import com.example.tracktastic.utils.VibrationUtil
import com.nvt.color.ColorPickerDialog


class ActivityAdapter(
    private val context: Context,
    val dataset: List<ClickerActivity>,
    val itemClickedCallbackSetTimer: (ClickerActivity) -> Unit,
    val itemClickedCallbackStopTimer: (ClickerActivity) -> Unit,
    val itemClickedCallbackChangeSettings: (ClickerActivity) -> Unit,
    val SettingsViewModel: SettingsViewModel,
) : RecyclerView.Adapter<ActivityAdapter.ItemViewHolder>() {


    inner class ItemViewHolder(val binding: ClickerItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ClickerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = dataset[position]

        holder.binding.tvClickerName.text = data.name
        holder.binding.tvClickerCount.text = data.value.toString()
        holder.binding.clickItem.setCardBackgroundColor(data.backgroundColor)

        //On click listeners
        holder.binding.btnClickerPlus.setOnClickListener {
            data.timesClicked++

            SettingsViewModel.timesClicked(data.id.toString(), data.timesClicked)
            SettingsViewModel.plusClicked(data)
            SettingsViewModel.lastClicked(data.id)
            data.lastClickedAt =
                "on:${Calculations.getCurrentDate()} at: ${Calculations.getCurrentTime()}"
            Log.d("timesclicked", data.timesClicked.toString())

            //vibration
            VibrationUtil.Vibration(context)
        }

        holder.binding.btnClickerMinus.setOnClickListener {
            //checking if value is not already 0
            if (data.value != 0) {
                data.timesClicked--
                SettingsViewModel.timesClicked(data.id.toString(), data.timesClicked)
                SettingsViewModel.minusClicked(data)
                SettingsViewModel.lastClicked(data.id)
            }
            //vibration
            VibrationUtil.Vibration(context)
        }

        holder.binding.llClickerName.setOnClickListener {
            if (holder.binding.cldetailSettings.visibility == View.VISIBLE) {
                holder.binding.cldetailSettings.visibility = View.GONE
                Log.d("onclicket", "tschüssi")
            } else {
                holder.binding.cldetailSettings.visibility = View.VISIBLE
                Log.d("onclicket", "moin")
            }
        }

        holder.binding.btnChangeColors.setOnClickListener {
            val colorPicker = ColorPickerDialog(
                context,
                ContextCompat.getColor(context, R.color.grey), // color init
                false, // true is show alpha
                object : ColorPickerDialog.OnColorPickerListener {
                    override fun onCancel(dialog: ColorPickerDialog?) {
                    }

                    override fun onOk(dialog: ColorPickerDialog?, colorPicker: Int) {
                        holder.binding.clickItem.setCardBackgroundColor(colorPicker)
                        SettingsViewModel.updateBackgroundColor(colorPicker, data)
                    }
                })
            colorPicker.show()
        }

        //callback functions
        holder.binding.btnSetTimer.setOnClickListener {
            itemClickedCallbackSetTimer(data)
        }

        holder.binding.btnStopTimer.setOnClickListener {
            itemClickedCallbackStopTimer(data)
        }

        holder.binding.btnItemSettings.setOnClickListener {
            itemClickedCallbackChangeSettings(data)
        }

    }

}


