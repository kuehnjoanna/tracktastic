package com.example.tracktastic.utils

import android.app.Dialog
import android.content.Context
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tracktastic.R

/*
statisticks
how long activity is going for aka you are doing it for x days
calendar data on which day you did the activity
you created the activity x dazs ago
longest streak:
longest pause
how many clicks ona day diagram maybe together
how much time in a day diagram maybe together



 */
class Timer {
    var isPomodoroOn = false
    var duration: Int = 0

    //pomodoro logic
    var pomodoroTimeSelected: Int = 0
    var pomodoroCountDown: CountDownTimer? = null
    var pomodoroProgress = 0
    var pauseOffSet: Long = 0
    var isPomodoroCountDownStart = false

    private val _pomodoroTime = MutableLiveData<String>()
    val pomodoroTime: LiveData<String>
        get() = _pomodoroTime
    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int>
        get() = _progress
    private val _selectedtime = MutableLiveData<Int>()
    val selectedtime: LiveData<Int>
        get() = _selectedtime


    fun setTimeFunction(context: Context) {
        val timeDialog = Dialog(context)
        timeDialog.setContentView(R.layout.add_dialog)
        val timeSet = timeDialog.findViewById<EditText>(R.id.etGetTime)
        timeDialog.findViewById<Button>(R.id.btnOk).setOnClickListener {
            if (timeSet.text.isEmpty()) {
                Toast.makeText(context, "Enter Time Duration", Toast.LENGTH_SHORT).show()
            } else {
                resetPomodoroTime()
                val givenTime = timeSet.text.toString().toInt() * 60
                val hours = givenTime / 3600
                val minutes = (givenTime % 3600) / 60
                val seconds = givenTime % 60

                val time = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                _pomodoroTime.postValue(time)
                //   binding.tvTimeLeft.text = time
                //      binding.btnPlayPause.text = "Start"
                pomodoroTimeSelected = timeSet.text.toString().toInt()
                // binding.pbTimer.max = timeSelected
                _selectedtime.postValue(pomodoroTimeSelected)
            }
            timeDialog.dismiss()
        }
        timeDialog.show()
    }

    fun resetPomodoroTime() {
        if (pomodoroCountDown != null) {
            pomodoroCountDown!!.cancel()
            countDuration(pomodoroProgress)
            pomodoroProgress = 0
            pomodoroTimeSelected = 0
            pauseOffSet = 0
            pomodoroCountDown = null
            isPomodoroOn = false
            //   binding.btnPlayPause.text = "Staert"
            isPomodoroCountDownStart = true
            _pomodoroTime.postValue("00:00:00")
            //   binding.pbTimer.progress = 0
            _progress.postValue(0)
            // binding.tvTimeLeft.text = "0"
        }
    }

    fun timePause() {
        if (pomodoroCountDown != null) {

            pomodoroCountDown!!.cancel()
            isPomodoroCountDownStart = false


        }
    }

    fun startTimerSetup() {
        if (isPomodoroCountDownStart == false)
        //   binding.btnPlayPause.text = "Pause"
            startPomodoroTimer(pauseOffSet)


    }

    fun startPomodoroTimer(pauseOffSetL: Long) {
        // Calculate the total time in seconds and progress
        val totalTimeInSeconds = pomodoroTimeSelected * 60
        val totalTimeInMillis = totalTimeInSeconds * 1000L - pauseOffSetL * 1000

        // Initialize progress
        //  binding.pbTimer.max = totalTimeInSeconds
        //binding.pbTimer.progress = totalTimeInSeconds - pauseOffSetL.toInt()
        _progress.postValue((totalTimeInSeconds - pauseOffSetL).toInt())
        _selectedtime.postValue(totalTimeInSeconds)
        pomodoroCountDown = object : CountDownTimer(totalTimeInMillis, 1000) {
            override fun onTick(p0: Long) {
                pomodoroProgress++
                val remainingTimeInSeconds = totalTimeInSeconds - pomodoroProgress
                val hours = remainingTimeInSeconds / 3600
                val minutes = (remainingTimeInSeconds % 3600) / 60
                val seconds = remainingTimeInSeconds % 60

                val time = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                _pomodoroTime.postValue(time)
                pauseOffSet = totalTimeInSeconds - p0 / 1000
                isPomodoroCountDownStart = true

                //     binding.pbTimer.progress = (totalTimeInSeconds - timeProgress).toInt()
                _progress.postValue((totalTimeInSeconds - pomodoroProgress))

                //   binding.tvTimeLeft.text = time
            }

            override fun onFinish() {

                resetPomodoroTime()
                //   Toast.makeText(requireContext(), "Times Up!", Toast.LENGTH_SHORT).show()
            }

        }.start()

    }

    fun countDuration(seconds: Int) {
        duration = 0

        val minutes = (seconds % 3600) / 60
        duration = minutes

        Log.d("duration", minutes.toString())
    }

    //stopwatch logic
    var isStopwatchRunning = false
    var stopwatchSeconds = 0
    private val _stopwatchTime = MutableLiveData<String>()
    val stopwatchTime: LiveData<String>
        get() = _stopwatchTime
    val stopwatchHandler = Handler(Looper.getMainLooper())
    var stopwatch = object : Runnable {
        override fun run() {
            stopwatchSeconds++
            val hours = stopwatchSeconds / 3600
            val minutes = (stopwatchSeconds % 3600) / 60
            val seconds = stopwatchSeconds % 60
            val time = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            _stopwatchTime.postValue(time)
            stopwatchHandler.postDelayed(this, 1000)
        }
    }

    fun startTimer() {

        if (!isStopwatchRunning) {
            stopwatchHandler.postDelayed(stopwatch, 1000)
            isStopwatchRunning = true
            Log.d("am i working?", "timer is working")
            _progress.postValue(10)
        }

    }

    fun stopTimer() {
        if (isStopwatchRunning == true) {

            stopwatchHandler.removeCallbacks(stopwatch)
            isStopwatchRunning = false

        }
    }

    fun resetTimer() {
        stopTimer()
        _progress.postValue(0)
        countDuration(stopwatchSeconds)
        stopwatchSeconds = 0
        _stopwatchTime.postValue(String.format("%02d:%02d:%02d", 0, 0, 0))

    }
}