package com.example.tracktastic.utils

import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Year
import java.util.Date

object Calculations {
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    fun getCurrentDate(): String{
    return cleanDate(Calendar.getInstance().get(Calendar.DAY_OF_MONTH),Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.YEAR))
    }
    fun getCurrentTime():String{
      return  cleanTime(Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE))
    }
    fun calculateTimeBetweenDates(startDate: String): String{


        val endDate = timeStampToString(System.currentTimeMillis())
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val date1 = simpleDateFormat.parse(startDate)
        val date2 = simpleDateFormat.parse(endDate)


        //check wether the number calculated is negative
        var isNegative = false

        var difference: Long = date2.time - date1.time
        //if the number is negative, convert it to positive(to not show user negative data)
        if(difference < 0){
            difference = -(difference)
            isNegative = true
            }

        //calculating minutes hourse days and so on
        val minutes = difference/60/1000
        val hours = difference/60/1000/60
        val days = (difference/60/1000/60) / 24
        val months = (difference/60/1000/60) / 24 / (365/12)
        val years = (difference/60/1000/60) / 24 / 365

        //check
        if (isNegative){
         return when {
                minutes < 240 -> "Starts in $minutes minutes"
                hours < 48 -> "Starts in $hours hourss"
                days < 61 -> "Starts in $days days"
                months < 24 -> "Starts in $months monthss"
                else -> "Starts in $years years"
            }
        }
        return when {
            minutes < 240 -> "$minutes minutes ago"
            hours < 48 -> "$hours hours ago"
            days < 61 -> "$days days ago"
            months < 24 -> "$months months ago"
            else -> "$years years ago"
        }
    }
    fun testToast(context: Context){
        Toast.makeText(context, "already clicked today!", Toast.LENGTH_SHORT).show()

    }

    //function to convert date into string and make it presentsable
    private fun timeStampToString(timestamp: Long):String {
        val stamp = Timestamp(timestamp)
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm") //hh makes am/pm HH makes 24h format
        val date = simpleDateFormat.format((Date(stamp.time)))

        return date.toString()
    }

    //time picker will return single digit number if it less than ten
    fun cleanDate(_day: Int, _month: Int, _year: Int): String{
        var day = _day.toString()
        var month = _month.toString()
        //we dont need year because picker cant go more that 100yrs back
        //making sure that anyday under 10 will get a 0 in front
        if (_day < 10){
            day = "0$_day"
        }
        if (_month < 9){
            month = "0${_month+1}"
        }
        //9 not 10 because calendar option receives the month starting index 0
        return "$day/$month/$_year"
    }
    fun cleanTime(_hour: Int, _minute: Int): String{
        var hour = _hour.toString()
        var minute = _minute.toString()
        //we dont need year because picker cant go more that 100yrs back
        //making sure that anyday under 10 will get a 0 in front
        if (_hour < 10){
            hour = "0$_hour"
        }
        if (_minute < 10){
            minute = "0${_minute}"
        }
        //9 not 10 because calendar option receives the month starting index 0
        return "$hour:$minute"
    }


    //this doesnt work for some reason
    fun getTimeCalendar(){
        val cal = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR_OF_DAY)
        minute = cal.get(Calendar.MINUTE)
    }

    fun getDateCalendar(){
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }



}