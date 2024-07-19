package com.example.tracktastic.ui.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.example.tracktastic.data.model.ClickerActivity
import com.example.tracktastic.databinding.ClickerItemBinding
import com.example.tracktastic.ui.viemodels.HomepageViewModel
import com.example.tracktastic.ui.viemodels.SettingsViewModel
import com.example.tracktastic.ui.viemodels.StatisticsViewModel
import com.example.tracktastic.utils.Calculations
import com.example.tracktastic.utils.VibrationUtil
import com.nvt.color.ColorPickerDialog


class ActivityAdapter(
    private val context: Context,
    val dataset: List<ClickerActivity>,
    val itemClickedCallback: (ClickerActivity) -> Unit,
    val itemClickedCallback2: (ClickerActivity) -> Unit,
    val itemClickedCallback3: (ClickerActivity) -> Unit,
    val viewModel: SettingsViewModel,
    val homeViewModel: HomepageViewModel,
    val statisticsViewModel: StatisticsViewModel
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

        holder.binding.btnClickerPlus.setOnClickListener {
            if (Calculations.getCurrentDate() == data.lastClickedAt) {
                Calculations.testToast(context)
            } else {
                data.timesClicked++
                viewModel.timesClicked(data.id.toString(), data.timesClicked)
                viewModel.plusClicked(data)
                viewModel.lastClicked(data.id)
                //  statisticsViewModel.addValueToStatsMap(1, 3)
                //dont delete before testing
                /*
                val name = Calendar.getInstance()
                    .get(android.icu.util.Calendar.YEAR).toString() + "." + (Calendar.getInstance()
                    .get(android.icu.util.Calendar.MONTH) + 1).toString()
                Log.d("name", name)
                viewModel.firestoreReference.collection("statistics").document(name)
                    .set(ActivitiesStatistic())

                 */
                data.lastClickedAt =
                    "on:${Calculations.getCurrentDate()} at: ${Calculations.getCurrentTime()}"
                Log.d("timesclicked", data.timesClicked.toString())
                Log.d("timesclicked", data.toString())
                //vibration
                VibrationUtil.Vibration(context)
                //animation
                Log.d("timesclicked", data.timesClicked.toString())
                holder.binding.tvClickerCount.animate().rotation(360f).setDuration(500)
                    .translationXBy(3f)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .start()
            }
        }

        holder.binding.btnClickerMinus.setOnClickListener {
            if (data.timesClicked != 0) {
                // val hm = data.timesClicked - data.decrement
                data.timesClicked--
                viewModel.timesClicked(data.id.toString(), data.timesClicked)
                viewModel.minusClicked(data)
                viewModel.lastClicked(data.id)
                holder.binding.tvClickerCount.text = data.timesClicked.toString()
                //animation
                Log.d("timesclicked", data.timesClicked.toString())
                holder.binding.tvClickerCount.animate().rotation(-360f).setDuration(500)
                    .translationXBy(3f)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .start()
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
                Color.BLACK, // color init
                true, // true is show alpha
                object : ColorPickerDialog.OnColorPickerListener {
                    override fun onCancel(dialog: ColorPickerDialog?) {
                        // handle click button Cancel
                    }

                    override fun onOk(dialog: ColorPickerDialog?, colorPicker: Int) {
                        // handle click button OK

                        holder.binding.clickItem.setCardBackgroundColor(colorPicker)
                        viewModel.updateBackgroundColor(colorPicker, data)
                    }
                })
            colorPicker.show()
        }
        holder.binding.btnSetTimer.setOnClickListener {
            itemClickedCallback(data)
        }
        holder.binding.btnStopTimer.setOnClickListener {
            itemClickedCallback2(data)
        }
        holder.binding.btnItemSettings.setOnClickListener {
            itemClickedCallback3(data)
        }

        /*
        holder.binding.btnColorFont.setOnClickListener {
            val colorPicker = ColorPickerDialog(
                context,
                Color.BLACK, // color init
                true, // true is show alpha
                object : ColorPickerDialog.OnColorPickerListener {
                    override fun onCancel(dialog: ColorPickerDialog?) {
                        // handle click button Cancel
                    }

                    override fun onOk(dialog: ColorPickerDialog?, colorPicker: Int) {
                        // handle click button OK
                        //  holder.binding.tvClickerName.setBackgroundColor(colorPicker)
                        holder.binding.clickItem.setStrokeColor(colorPicker)
                        Log.d("picker", "$colorPicker ")
                    }
                })
            colorPicker.show()
        }

         */

        /*
        holder.binding.cldetailSettings.visibility = View.VISIBLE
        holder.binding.cldetailSettings.alpha = 0f
        holder.binding.cldetailSettings.animate().setDuration(1500).alpha(0.5f).withEndAction {
            holder.binding.cldetailSettings.alpha = 1f
        }
        holder.binding.btnSaveChanges.setOnClickListener {

            holder.binding.cldetailSettings.visibility = View.GONE
        }
        holder.binding.btncancel.setOnClickListener {

            holder.binding.cldetailSettings.visibility = View.GONE
        }

         */
    }

}


