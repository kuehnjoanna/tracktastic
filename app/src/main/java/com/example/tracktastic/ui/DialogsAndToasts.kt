package com.example.tracktastic.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Toast
import com.example.tracktastic.databinding.ConfirmDialogBinding

object DialogsAndToasts {
    fun showToast(message: Int, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun addDialog(
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

        binding.btnConfirm.setOnClickListener {
            showToast(dialogMessage, context)
            function()
        }
        binding.btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }
}