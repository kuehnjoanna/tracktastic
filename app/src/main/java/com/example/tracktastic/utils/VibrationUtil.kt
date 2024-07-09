package com.example.tracktastic.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast

object VibrationUtil {

    fun Vibration(ctx: Context) {
        if (Build.VERSION.SDK_INT >= 31) {
            val vibratorManager =
                ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
        } else {
            val v = ctx.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= 26) {
                v.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
            } else {
                v.vibrate(200L)
            }
        }

        Toast.makeText(ctx, "I am VIBRATIIIING!", Toast.LENGTH_SHORT).show()
    }
}