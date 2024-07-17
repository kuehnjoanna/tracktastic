package com.example.tracktastic.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import coil.load
import com.example.tracktastic.data.model.ClickerActivity
import com.example.tracktastic.databinding.FragmentHomeBinding
import com.example.tracktastic.ui.adapter.ActivityAdapter
import com.example.tracktastic.ui.viemodels.HomepageViewModel
import com.example.tracktastic.ui.viemodels.LoginViewModel
import com.example.tracktastic.ui.viemodels.SettingsViewModel
import com.example.tracktastic.ui.viemodels.StatisticsViewModel
import java.util.Collections


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: LoginViewModel by activityViewModels()
    private val statisticsViewModel: StatisticsViewModel by activityViewModels()
    private val homepageViewModel: HomepageViewModel by activityViewModels()
    lateinit var adapter: ActivityAdapter
    private val settingsViewModel: SettingsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }
    //binding.pbTimer.progress = (totalTimeInSeconds - timeProgress).toInt()

    //binding.pbTimer.max = totalTimeInSeconds
    //binding.pbTimer.progress = totalTimeInSeconds - pauseOffSetL.toInt()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //    binding.pbTimer.max = homepageViewModel.timerLogic.pomodoroTimeSelected
        //  binding.pbTimer.progress = homepageViewModel.timerLogic.pomodoroTimeSelected - homepageViewModel.timerLogic.pauseOffSet.toInt()
//datasrtore


//
        settingsViewModel.retrieveDataFromDatabase()
        settingsViewModel.retrieveListFromFirestore()
        binding.text.text = "hello,"
        settingsViewModel.firebaseWallpaperUrl.observe(viewLifecycleOwner) {
            //    binding.homelayout.load(settingsViewModel.firebaseWallpaperUrl.value)
            Log.d("firebasewallpa", settingsViewModel.firebaseWallpaperUrl.value!!)


        }
        settingsViewModel.firebaseAvatarUrl.observe(viewLifecycleOwner) {
            binding.BTNProfile.load(settingsViewModel.firebaseAvatarUrl.value)
        }
        settingsViewModel.firebaseName.observe(viewLifecycleOwner) {
            binding.text.text = "Hello, " + settingsViewModel.firebaseName.value.toString()
        }
        ///timer

        homepageViewModel.timerLogic.stopwatchTime.observe(viewLifecycleOwner) {
            binding.tvTimeLeft.text = it.toString()
            Log.d("timerlogic = time", homepageViewModel.timerLogic.stopwatchTime.value.toString())

        }
        homepageViewModel.timerLogic.pomodoroTime.observe(viewLifecycleOwner) {
            binding.tvTimeLeft.text = it.toString()
            Log.d("timerlogic = time", homepageViewModel.timerLogic.pomodoroTime.value.toString())


        }
        homepageViewModel.timerLogic.progress.observe(viewLifecycleOwner) {
            // binding.pbTimer.progress = it.toFloat()
            if (homepageViewModel.timerLogic.isPomodoroOn == true) {
                binding.pbTimer.setProgressWithAnimation(it.toFloat(), 1000)
            } else if (homepageViewModel.timerLogic.isStopwatchRunning == true) {

                binding.pbTimer.indeterminateMode = true
            }

        }
        homepageViewModel.timerLogic.selectedtime.observe(viewLifecycleOwner) {
            binding.pbTimer.progressMax = it.toFloat()
        }


        //stopwatch
        binding.playPauseTimer.setOnClickListener {
            if (homepageViewModel.timerLogic.isPomodoroOn == false) {
                if (homepageViewModel.timerLogic.isStopwatchRunning == true) {
                    homepageViewModel.timerLogic.stopTimer()
                    binding.pbTimer.indeterminateMode = false

                } else {
                    homepageViewModel.timerLogic.startTimer()
                }
            } else {
                if (homepageViewModel.timerLogic.isPomodoroCountDownStart == true) {
                    homepageViewModel.timerLogic.timePause()
                } else {
                    homepageViewModel.timerLogic.startTimerSetup()
                }

            }

        }
        binding.btnStopTimer.setOnClickListener {
            if (homepageViewModel.timerLogic.isPomodoroOn == false) {
                homepageViewModel.timerLogic.resetTimer()
                binding.pbTimer.indeterminateMode = false
                homepageViewModel.timerLogic.resetPomodoroTime()
                binding.tvTimeLeft.text = "00:00:00"
                Log.d("dur", homepageViewModel.timerLogic.duration.toString())
            } else {
                homepageViewModel.timerLogic.resetPomodoroTime()
                if (homepageViewModel.isSelectedItemClicked == true) {
                    homepageViewModel.addDuration()
                }
                Log.d("dur2", homepageViewModel.timerLogic.duration.toString())
                //add dialog to which activity should the time be added?

            }
        }



        viewModel.currentUser.observe(viewLifecycleOwner) {
            binding.BTNProfile.setOnClickListener {

                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSettingsFragment())

            }
        }

        val helper: SnapHelper = PagerSnapHelper()
        helper.attachToRecyclerView(binding.recyclerView)


        val itemClickedCallback: (ClickerActivity) -> Unit = {
            homepageViewModel.selectedActivityItem(it)
            homepageViewModel.timerLogic.isPomodoroOn = true
            homepageViewModel.timerLogic.setTimeFunction(requireContext())

        }
        val itemClickedCallback2: (ClickerActivity) -> Unit = {
            homepageViewModel.selectedActivityItem(it)
            homepageViewModel.timerLogic.isPomodoroOn = false
            homepageViewModel.timerLogic.resetPomodoroTime()
            homepageViewModel.addDuration()
        }


        settingsViewModel.repository.clicketestlist.observe(viewLifecycleOwner) {
            if (settingsViewModel.repository.clicketestlist.value!!.size == 0) {
                binding.noactivities.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
            binding.noactivities.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            adapter = ActivityAdapter(
                requireContext(),
                settingsViewModel.repository.clicketestlist.value!!,
                itemClickedCallback,
                itemClickedCallback2,
                SettingsViewModel(),
                HomepageViewModel(),
                StatisticsViewModel()
            )
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            //  binding.recyclerView.layoutManager = StackCardLayoutManager(settingsViewModel.repository.clicketestlist.value!!.size)

            // binding.recyclerView.layoutManager = StackLayoutManager(StackLayoutManager.VERTICAL, true, 70, false)


            binding.recyclerView.adapter = adapter
////////////////////////////////////////////////////// D E L E T E    B E I    S W I P E

            ItemTouchHelper(object :
                ItemTouchHelper.SimpleCallback(
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val adapter = recyclerView.adapter as ActivityAdapter
                    val startPosition = viewHolder.adapterPosition

                    val endPosition = target.adapterPosition


                    Collections.swap(
                        settingsViewModel.repository.clicketestlist.value!!,
                        startPosition,
                        endPosition
                    )
                    adapter.notifyItemMoved(startPosition, endPosition)
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val id = adapter.dataset.get(viewHolder.adapterPosition)
                    // settingsViewModel.repository.testclickerlist.remove(id)
                    AlertDialog.Builder(requireContext())
                        .setTitle("Are you sure you want to delete this activity?")
                        .setPositiveButton("Yes") { _, _ ->

                            settingsViewModel.firestoreReference.collection("activities")
                                .document(id.id.toString())
                                .delete()

                            adapter.notifyItemRemoved(viewHolder.adapterPosition)  // Notify adapter about item removal

                            Toast.makeText(requireContext(), "deleted", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("No") { _, _ ->

                            adapter.notifyDataSetChanged()
                        }
                        .show()

                }
            }).attachToRecyclerView(binding.recyclerView)


        }
    }
    /*
        ////pomodoro
        //countdown
        private var timeSelected: Int = 0
        private var timeCountDown: CountDownTimer? = null
        private var timeProgress = 0
        private var pauseOffSet: Long = 0
        private var isCountDownStart = true

        private fun resetPomodoroTime() {
            if (timeCountDown != null) {
                timeCountDown!!.cancel()
                timeProgress = 0
                timeSelected = 0
                pauseOffSet = 0
                timeCountDown = null
                //   binding.btnPlayPause.text = "Staert"
                isCountDownStart = true
                binding.pbTimer.progress = 0
                binding.tvTimeLeft.text = "0"
            }
        }

        private fun timePause() {
            if (timeCountDown != null) {
                timeCountDown!!.cancel()
            }
        }

        private fun startTimerSetup() {
            if (timeSelected > timeProgress) {
                if (isCountDownStart) {
                    //   binding.btnPlayPause.text = "Pause"
                    startPomodoroTimer(pauseOffSet)
                    isCountDownStart = false
                } else {
                    isCountDownStart = true
                    // binding.btnPlayPause.text = "Resume"
                    timePause()
                }
            } else {

                Toast.makeText(requireContext(), "Enter Time", Toast.LENGTH_SHORT).show()
            }
        }

        private fun startPomodoroTimer(pauseOffSetL: Long) {
            // Calculate the total time in seconds and progress
            val totalTimeInSeconds = timeSelected * 60
            val totalTimeInMillis = totalTimeInSeconds * 1000L - pauseOffSetL * 1000

            // Initialize progress
            binding.pbTimer.max = totalTimeInSeconds
            binding.pbTimer.progress = totalTimeInSeconds - pauseOffSetL.toInt()

            timeCountDown = object : CountDownTimer(totalTimeInMillis, 1000) {
                override fun onTick(p0: Long) {
                    timeProgress++
                    val remainingTimeInSeconds = totalTimeInSeconds - timeProgress
                    val hours = remainingTimeInSeconds / 3600
                    val minutes = (remainingTimeInSeconds % 3600) / 60
                    val seconds = remainingTimeInSeconds % 60

                    val time = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                    pauseOffSet = totalTimeInSeconds - p0 / 1000
                    binding.pbTimer.progress = (totalTimeInSeconds - timeProgress).toInt()
                    binding.tvTimeLeft.text = time
                }

                override fun onFinish() {
                    homepageViewModel.timerLogic.isPomodoroOn = false
                    resetPomodoroTime()
                    Toast.makeText(requireContext(), "Times Up!", Toast.LENGTH_SHORT).show()
                }

            }.start()
        }


        private fun setTimeFunction() {
            val timeDialog = Dialog(requireContext())
            timeDialog.setContentView(R.layout.add_dialog)
            val timeSet = timeDialog.findViewById<EditText>(R.id.etGetTime)
            timeDialog.findViewById<Button>(R.id.btnOk).setOnClickListener {
                if (timeSet.text.isEmpty()) {
                    Toast.makeText(requireContext(), "Enter Time Duration", Toast.LENGTH_SHORT).show()
                } else {
                    homepageViewModel.timerLogic.resetPomodoroTime()
                    val givenTime = timeSet.text.toString().toInt() * 60
                    val hours = givenTime / 3600
                    val minutes = (givenTime % 3600) / 60
                    val seconds = givenTime % 60

                    val time = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                    binding.tvTimeLeft.text = time
                    //      binding.btnPlayPause.text = "Start"
                    timeSelected = timeSet.text.toString().toInt()
                    binding.pbTimer.max = timeSelected
                }
                timeDialog.dismiss()
            }
            timeDialog.show()
        }

     */
}