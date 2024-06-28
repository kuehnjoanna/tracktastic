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
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.UP
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
        settingsViewModel.firebaseWallpaperUrl.observe(viewLifecycleOwner) {
            binding.homelayout.load(settingsViewModel.firebaseWallpaperUrl.value)
            Log.d("firebasewallpa", settingsViewModel.firebaseWallpaperUrl.value!!)

        }

        viewModel.currentUser.observe(viewLifecycleOwner) {
            binding.BTNSignOut.setOnClickListener {
                viewModel.logOut()
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())

            }

            binding.fab.setOnClickListener {

                FirebaseFirestore.getInstance().collection("users")
                    .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
                    .collection("activities").document("lol").addSnapshotListener { value, error ->
                    value?.data.toString()

                    binding.text.text = value?.data?.get("lol").toString()


                }

                //   findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSetNewFragment())
            }
        }

        val helper: SnapHelper = PagerSnapHelper()
        helper.attachToRecyclerView(binding.recyclerView)
        val itemClickedCallback: (ClickerActivity) -> Unit = {
            //  settingsViewModel.selectedActivityItem(it)

        }


        settingsViewModel.repository.clicketestlist.observe(viewLifecycleOwner) {

            adapter = ActivityAdapter(settingsViewModel.repository.clicketestlist.value!!, itemClickedCallback)
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.adapter = adapter
/////////////////////////////

            ItemTouchHelper(object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    TODO("Not yet implemented")
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

                            adapter.notifyItemRemoved(viewHolder.adapterPosition)
                        }
                        .show()

                }
            }).attachToRecyclerView(binding.recyclerView)

            /////////////////////
            ItemTouchHelper(object :
                ItemTouchHelper.SimpleCallback(UP or DOWN, 0) {
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


                }
            }).attachToRecyclerView(binding.recyclerView)

        }
    }


}