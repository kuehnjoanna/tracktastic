package com.example.tracktastic.ui

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tracktastic.R
import com.example.tracktastic.databinding.ConfirmDialogBinding
import com.example.tracktastic.databinding.EditDialogBinding
import com.tapadoo.alerter.Alerter

object DialogsAndToasts {

    private val _editText = MutableLiveData<String>()
    val editText: LiveData<String>
        get() = _editText

    fun createInAppAlert(activity: Activity, alertTitle: Int, alert: Int) {

        Alerter.Companion.create(activity).setTitle(alertTitle).setIcon(R.drawable.tracktastic_5)
            .setText(alert).setDuration(4000).show()

    }

    fun showToast(message: Int, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun confirmDialog(
        context: Context,
        dialogMessage: Int,
        dialogbinding: ConfirmDialogBinding,
        function: () -> Unit
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding = dialogbinding
        dialog.setCancelable(false)
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.tvMessage.text = context.getString(dialogMessage)
        Log.d("string", context.getString(dialogMessage))
        binding.btnConfirm.setOnClickListener {
            showToast(dialogMessage, context)
            function()
            dialog.dismiss()
        }
        binding.btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    fun addEditDialog(
        context: Context,
        dialogMessage: Int,
        dialogbinding: EditDialogBinding,
        function: () -> Unit
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding = dialogbinding
        dialog.setCancelable(false)
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.tvMessage.text = context.getString(dialogMessage)


        binding.btnConfirm.setOnClickListener {
            if (binding.ETeditText.text.isNotEmpty()) {
                _editText.postValue(binding.ETeditText.text.toString())
                function()
                DialogsAndToasts.showToast(R.string.sucessfully_updated, context)

            } else {
                DialogsAndToasts.showToast(R.string.field_cant_be_empty, context)
            }
            dialog.dismiss()
        }
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }
}