package com.example.tracktastic.ui

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.tracktastic.R
import com.example.tracktastic.databinding.FragmentTimerBinding

class TimerFragment : Fragment() {
private lateinit var binding: FragmentTimerBinding
    private var timeSelected : Int = 0
    private var timeCountDown: CountDownTimer? = null
    private var timeProgress = 0
    private var pauseOffSet: Long = 0
    private var isStart = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
   binding = FragmentTimerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener {
            setTimeFunction()
        }
        binding.btnPlayPause.setOnClickListener {
            startTimerSetup()
        }
        binding.ibReset.setOnClickListener {
            resetTime()
        }
        binding.tvAddTime.setOnClickListener {
            addExtraTime()
        }

    }
    private fun addExtraTime()
    {
        if (timeSelected!=0)
        {
            timeSelected+=15
            binding.pbTimer.max = timeSelected
            timePause()
            startTimer(pauseOffSet)
            Toast.makeText(requireContext(),"15 sec added",Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetTime()
    {
        if (timeCountDown!=null)
        {
            timeCountDown!!.cancel()
            timeProgress=0
            timeSelected=0
            pauseOffSet=0
            timeCountDown=null
            binding.btnPlayPause.text = "Staert"
            isStart = true
            binding.pbTimer.progress = 0
            binding.tvTimeLeft.text = "0"
        }
    }

    private fun timePause()
    {
        if (timeCountDown!=null)
        {
            timeCountDown!!.cancel()
        }
    }

    private fun startTimerSetup()
    {
        if (timeSelected>timeProgress)
        {
            if (isStart)
            {
                binding.btnPlayPause.text = "Pause"
                startTimer(pauseOffSet)
                isStart = false
            }
            else
            {
                isStart =true
                binding.btnPlayPause.text = "Resume"
                timePause()
            }
        }
        else
        {

            Toast.makeText(requireContext(),"Enter Time",Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTimer(pauseOffSetL: Long)
    {
        binding.pbTimer.progress = timeProgress
        timeCountDown = object :CountDownTimer(
            (timeSelected*1000).toLong() - pauseOffSetL*1000, 1000)
        {
            override fun onTick(p0: Long) {
                timeProgress++
                pauseOffSet = timeSelected.toLong()- p0/1000
                binding.pbTimer.progress = timeSelected-timeProgress
                binding.tvTimeLeft.text = (timeSelected - timeProgress).toString()
            }

            override fun onFinish() {
                resetTime()
                Toast.makeText(requireContext(),"Times Up!", Toast.LENGTH_SHORT).show()
            }

        }.start()
    }


    private fun setTimeFunction()
    {
        val timeDialog = Dialog(requireContext())
        timeDialog.setContentView(R.layout.add_dialog)
        val timeSet = timeDialog.findViewById<EditText>(R.id.etGetTime)
        timeDialog.findViewById<Button>(R.id.btnOk).setOnClickListener {
            if (timeSet.text.isEmpty())
            {
                Toast.makeText(requireContext(),"Enter Time Duration",Toast.LENGTH_SHORT).show()
            }
            else
            {
                resetTime()
                binding.tvTimeLeft.text = timeSet.text
                binding.btnPlayPause.text = "Start"
                timeSelected = timeSet.text.toString().toInt()
                binding.pbTimer.max = timeSelected
            }
            timeDialog.dismiss()
        }
        timeDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(timeCountDown!=null)
        {
            timeCountDown?.cancel()
            timeProgress=0
        }
    }


}