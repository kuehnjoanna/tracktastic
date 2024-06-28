package com.example.tracktastic.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.tracktastic.data.model.ClickerActivity
import com.example.tracktastic.databinding.ClickerItemBinding
import com.example.tracktastic.ui.viemodels.SettingsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ActivityAdapter(
    val dataset: List<ClickerActivity>,
    val itemClickedCallback: (ClickerActivity) -> Unit,
) : RecyclerView.Adapter<ActivityAdapter.ItemViewHolder>() {


    inner class ItemViewHolder(val binding: ClickerItemBinding) : RecyclerView.ViewHolder(binding.root)

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
        holder.binding.tvClickerCount.text = data.timesClicked.toString()

        holder.binding.btnClickerPlus.setOnClickListener {
            data.timesClicked++
            holder.binding.tvClickerCount.text = data.timesClicked.toString()
            Log.d("timesclicked", data.timesClicked.toString())
        }

        holder.binding.btnClickerMinus.setOnClickListener {
            if (data.timesClicked != 0) {
                data.timesClicked--
                holder.binding.tvClickerCount.text = data.timesClicked.toString()
                Log.d("timesclicked", data.timesClicked.toString())
            }
        }

        holder.binding.tvClickerName.setOnClickListener {
            holder.binding.tvClickerName.visibility = View.GONE
            holder.binding.etClickerName.visibility = View.VISIBLE

            holder.binding.etClickerName.hint = holder.binding.tvClickerName.text.toString()

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