package com.example.tracktastic.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tracktastic.databinding.EditDialogBinding

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

    val duration2: LiveData<Int>
        get() = _duration2
    private val _duration2 = MutableLiveData<Int>()

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

    var _notification = MutableLiveData<String?>(null)
    val notification: LiveData<String?>
        get() = _notification
    val selectedtime: LiveData<Int>
        get() = _selectedtime


    fun setTimeFunction(context: Context, dialogBinding: EditDialogBinding) {
        val binding = dialogBinding
        val timeDialog = Dialog(context)
        timeDialog.setContentView(binding.root)
        timeDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.tvMessage.text = "Enter Time Duration"
        binding.editTitle.text = "Countdown Timeer"
        val timeSet = binding.ETeditText
        binding.btnConfirm.setOnClickListener {
            if (timeSet.text.isEmpty()) {
                Toast.makeText(context, "Enter Time Duration", Toast.LENGTH_SHORT).show()
            } else {
                resetPomodoroTime()
                val givenTime = timeSet.text.toString().toInt() * 60
                val hours = givenTime / 3600
                val minutes = (givenTime % 3600) / 60
                val seconds = givenTime % 60
                isPomodoroOn = true
                val time = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                _pomodoroTime.postValue(time)
                pomodoroTimeSelected = timeSet.text.toString().toInt()
                _selectedtime.postValue(pomodoroTimeSelected)
            }
            timeDialog.dismiss()
        }
        binding.btnCancel.setOnClickListener {
            timeDialog.dismiss()
            isPomodoroOn = false
        }
        timeDialog.show()
    }

    fun resetPomodoroTime() {
        if (pomodoroCountDown != null) {
            countDuration(pomodoroProgress)
            pomodoroCountDown!!.cancel()


            pomodoroTimeSelected = 0
            pauseOffSet = 0
            pomodoroCountDown = null
            isPomodoroOn = false
            //   binding.btnPlayPause.text = "Staert"
            _notification.postValue("You just tracked ${countDuration(pomodoroProgress)} minutes, great job!")
            isPomodoroCountDownStart = false
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
        pomodoroProgress = 0
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
                //notification that is observed in main activity so the send notification would work
                _notification.postValue("You just tracked ${duration2.value} minutes, great job!")
                countDuration(pomodoroProgress)
                resetPomodoroTime()

                //   Toast.makeText(requireContext(), "Times Up!", Toast.LENGTH_SHORT).show()
            }

        }.start()

    }

    fun countDuration(seconds: Int): Int {
        duration = 0
        _duration2.postValue(0)
        val minutes = (seconds % 3600) / 60
        duration = minutes
        _duration2.postValue(minutes)

        Log.d("durationminutes", minutes.toString())
        Log.d("duration", duration.toString())
        Log.d("durationlivedata", duration2.value.toString())
        return duration
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
        _notification.postValue("You just tracked ${countDuration(stopwatchSeconds)} minutes, great job!")
        _progress.postValue(0)
        countDuration(stopwatchSeconds)
        stopwatchSeconds = 0
        _stopwatchTime.postValue(String.format("%02d:%02d:%02d", 0, 0, 0))

    }
}