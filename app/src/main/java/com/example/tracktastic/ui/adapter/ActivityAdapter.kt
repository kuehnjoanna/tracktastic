package com.example.tracktastic.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.example.tracktastic.data.model.ClickerActivity
import com.example.tracktastic.databinding.ClickerItemBinding
import com.example.tracktastic.ui.viemodels.SettingsViewModel
import com.example.tracktastic.utils.Calculations
import com.example.tracktastic.utils.VibrationUtil

class ActivityAdapter(
    private val context: Context,
    val dataset: List<ClickerActivity>,
    val itemClickedCallback: (ClickerActivity) -> Unit,
    val viewModel: SettingsViewModel
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

        holder.binding.btnClickerPlus.setOnClickListener {
            if (Calculations.getCurrentDate() == data.lastClickedAt){
                Calculations.testToast(context)
            }else {
                data.timesClicked++
                viewModel.timesClicked(data.id.toString(), data.timesClicked)
                viewModel.plusClicked(data)
                viewModel.lastClicked(data.id)
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
                data.timesClicked--
                viewModel.timesClicked(data.id.toString(), data.timesClicked)
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
        holder.binding.llClickerName.setOnClickListener{
            Log.d("onclicket", "knok, knock")

            holder.binding.llClickerName.setOnClickListener{
                if (holder.binding.etValue.editableText.isNotEmpty()){
                   val value = holder.binding.etValue.editableText.toString()
                    Log.d("onclicket", value)
                    viewModel.updateClickerValue(data, value.toInt())
                }
                if (holder.binding.etIncrement.text != null){
                    viewModel.updateClickerIncrement(data, holder.binding.etIncrement.text.toString().toInt() )
                }
                if (holder.binding.etDecrement.text != null){
                    viewModel.updateClickerDecrement(data, holder.binding.etDecrement.text.toString().toInt() )
                }


            }

        }




        holder.binding.llClicker.setOnClickListener {
            itemClickedCallback(data)
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


}