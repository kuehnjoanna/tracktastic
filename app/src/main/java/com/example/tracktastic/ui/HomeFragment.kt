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
import com.example.tracktastic.databinding.FragmentHomeBinding
import com.example.tracktastic.ui.adapter.ActivityAdapter
import com.example.tracktastic.ui.viemodels.LoginViewModel
import com.example.tracktastic.ui.viemodels.SettingsViewModel
import java.util.Collections


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: LoginViewModel by activityViewModels()
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

        settingsViewModel.retrieveDataFromDatabase()
        settingsViewModel.retrieveListFromFirestore()
        binding.text.text = "hello,"
        settingsViewModel.firebaseWallpaperUrl.observe(viewLifecycleOwner) {
            binding.homelayout.load(settingsViewModel.firebaseWallpaperUrl.value)
            Log.d("firebasewallpa", settingsViewModel.firebaseWallpaperUrl.value!!)


        }
        settingsViewModel.firebaseAvatarUrl.observe(viewLifecycleOwner){
            binding.BTNSignOut.load(settingsViewModel.firebaseAvatarUrl.value)
        }
        settingsViewModel.firebaseName.observe(viewLifecycleOwner){
            binding.text.text = "Hello, " + settingsViewModel.firebaseName.value.toString()
        }



        viewModel.currentUser.observe(viewLifecycleOwner) {
            binding.BTNSignOut.setOnClickListener {
                viewModel.logOut()
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())

            }
        }

        val helper: SnapHelper = PagerSnapHelper()
        helper.attachToRecyclerView(binding.recyclerView)
        val itemClickedCallback: (ClickerActivity) -> Unit = {
              settingsViewModel.selectedActivityItem(it)


        }
        binding.fab.setOnClickListener {
        findNavController().navigate(R.id.timerFragment)
        }
        binding.tvSeeMore.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSettingsFragment())
        }


        settingsViewModel.repository.clicketestlist.observe(viewLifecycleOwner) {
if (settingsViewModel.repository.clicketestlist.value == null){

}
            adapter = ActivityAdapter(requireContext(), settingsViewModel.repository.clicketestlist.value!!, itemClickedCallback, SettingsViewModel())
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.adapter = adapter
////////////////////////////////////////////////////// D E L E T E    B E I    S W I P E

            ItemTouchHelper(object :
                ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val adapter = recyclerView.adapter as ActivityAdapter
                    val startPosition = viewHolder.adapterPosition

                    val endPosition = target.adapterPosition


                    Collections.swap(settingsViewModel.repository.clicketestlist.value!!, startPosition, endPosition)
                    adapter.notifyItemMoved(startPosition,endPosition)
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val id = adapter.dataset.get(viewHolder.adapterPosition)
               // settingsViewModel.repository.testclickerlist.remove(id)
                    AlertDialog.Builder(requireContext())
                        .setTitle("Are you sure you want to delete this activity?")
                        .setPositiveButton("Yes") { _, _ ->

                            settingsViewModel.firestoreReference.collection("activities").document(id.name)
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



}