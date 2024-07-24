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
import com.example.tracktastic.R
import com.example.tracktastic.data.model.ClickerActivity
import com.example.tracktastic.databinding.EditDialogBinding
import com.example.tracktastic.databinding.FragmentHomeBinding
import com.example.tracktastic.ui.adapter.ActivityAdapter
import com.example.tracktastic.ui.viemodels.HomepageViewModel
import com.example.tracktastic.ui.viemodels.LoginViewModel
import com.example.tracktastic.ui.viemodels.SettingsViewModel
import java.util.Collections


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: LoginViewModel by activityViewModels()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//datasrtore

        var time = 0
//
        settingsViewModel.retrieveDataFromDatabase()
        settingsViewModel.retrieveListFromFirestore()
        binding.text.text = "hello,"
        homepageViewModel.timerLogic.duration2.observe(viewLifecycleOwner) {
            time = it
        }


        settingsViewModel.firebaseWallpaperUrl.observe(viewLifecycleOwner) {
            binding.homelayout.load(settingsViewModel.firebaseWallpaperUrl.value)
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
                if (homepageViewModel.isSelectedItemClicked == true) {
                    //  homepageViewModel.addDuration()
                    homepageViewModel.addDuration(homepageViewModel.timerLogic.duration)
                    Log.d("durationTIME", time.toString())

                }
                homepageViewModel.timerLogic.resetPomodoroTime()

                Log.d("dur2", homepageViewModel.timerLogic.duration.toString())
                //add dialog to which activity should the time be added?

            }
        }



        viewModel.currentUser.observe(viewLifecycleOwner) {
            binding.BTNProfile.setOnClickListener {
                //findNavController().navigate(R.id.blankFragment)
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSettingsFragment())

            }
        }

        val helper: SnapHelper = PagerSnapHelper()
        helper.attachToRecyclerView(binding.recyclerView)


        val itemClickedCallbackSetTimer: (ClickerActivity) -> Unit = {
            homepageViewModel.selectedActivityItem(it)
            homepageViewModel.timerLogic.isPomodoroOn = true

            homepageViewModel.timerLogic.setTimeFunction(
                requireContext(),
                EditDialogBinding.inflate(layoutInflater)
            )


        }


        val itemClickedCallbackStopTimer: (ClickerActivity) -> Unit = {
            if (homepageViewModel.timerLogic.isPomodoroOn == true) {
                homepageViewModel.selectedActivityItem(it)
                // homepageViewModel.addDuration()
                homepageViewModel.addDuration(homepageViewModel.timerLogic.duration)
                homepageViewModel.addDuration(homepageViewModel.timerLogic.duration)
                Log.d("durationTimecallback", time.toString())
                Log.d("durationTimecallback2", homepageViewModel.timerLogic.duration.toString())
                homepageViewModel.timerLogic.isPomodoroOn = false
                homepageViewModel.timerLogic.resetPomodoroTime()

                DialogsAndToasts.createInAppAlert(
                    requireActivity(),
                    R.string.app_name,
                    R.string.duration_updated
                )
            }

        }
        val itemClickedCallbackChangeSettings: (ClickerActivity) -> Unit = {
            settingsViewModel.selectedActivityItem(it)
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToUpdateFragment())
        }
        val itemClickedCallback4: (ClickerActivity) -> Unit = {

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
                itemClickedCallbackSetTimer,
                itemClickedCallbackStopTimer,
                itemClickedCallbackChangeSettings,
                SettingsViewModel()
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

                            Toast.makeText(requireContext(), "deleted", Toast.LENGTH_SHORT)
                                .show()
                        }
                        .setNegativeButton("No") { _, _ ->

                            adapter.notifyDataSetChanged()
                        }
                        .show()

                }
            }).attachToRecyclerView(binding.recyclerView)


        }

    }
}