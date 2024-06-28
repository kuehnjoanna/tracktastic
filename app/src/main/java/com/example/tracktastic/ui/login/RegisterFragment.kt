package com.example.tracktastic.ui.login

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.tracktastic.R
import com.example.tracktastic.data.Repository
import com.example.tracktastic.databinding.FragmentRegisterBinding
import com.example.tracktastic.ui.viemodels.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class RegisterFragment : Fragment() {

    //setting viewbind and viewmodel connection
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //observing current user for fast reaction
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->

            if (user == null) {
                //User ist nicht eingeloggt
                Log.d("CurrentUser", "Kein User eingeloggt")
            } else {
                //User ist eingeloggt
                Log.d("CurrentUser", user.uid)

                viewModel.loadWallpaper()
                findNavController().navigate(R.id.homeFragment)
            }
            binding.profilPicture1.load("https://avatar.iran.liara.run/public/66")
            binding.profilPicture1.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.repository.upload("https://avatar.iran.liara.run/public/66", "profilePicUrl") {
                        if (it != null) {
                            Log.d(
                                "upload function",
                                "success, ${Repository.uri}"
                            )// diese ist immer noch leer, aber*
                        } else {
                            Log.d("upload function", "fail, ${Repository.uri}")
                        }

                    }
                }
            }
            binding.profilPicture2.load("https://avatar.iran.liara.run/public/6")
            binding.profilPicture2.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.repository.upload("https://avatar.iran.liara.run/public/6", "profilePicUrl") {
                        if (it != null) {
                            Log.d(
                                "upload function",
                                "success, ${Repository.uri}"
                            )// diese ist immer noch leer, aber*
                        } else {
                            Log.d("upload function", "fail, ${Repository.uri}")
                        }

                    }
                }
            }
            binding.profilPicture3.load("https://avatar.iran.liara.run/public/95")
            binding.profilPicture3.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.repository.upload("https://avatar.iran.liara.run/public/95", "profilePicUrl") {
                        if (it != null) {
                            Log.d(
                                "upload function",
                                "success, ${Repository.uri}"
                            )// diese ist immer noch leer, aber*
                        } else {
                            Log.d("upload function", "fail, ${Repository.uri}")
                        }

                    }
                }
            }
            binding.profilPicture4.load("https://avatar.iran.liara.run/public/22")
            binding.profilPicture4.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.repository.upload("https://avatar.iran.liara.run/public/22", "profilePicUrl") {
                        if (it != null) {
                            Log.d(
                                "upload function",
                                "success, ${Repository.uri}"
                            )// diese ist immer noch leer, aber*
                        } else {
                            Log.d("upload function", "fail, ${Repository.uri}")
                        }

                    }
                }
            }
            binding.BTNcreateNewUser.setOnClickListener {

                val userName = binding.ETuserNameNew.text.toString()
                val userEmail = binding.ETuserEmailNew.text.toString()
                val userPassword = binding.ETuserPasswordNew.text.toString()
                //creating new account if password requirements are met
                if (viewModel.isValidPassword(userPassword) == true) {
                    viewModel.signUpWithFirebase(userEmail, userPassword, userName)
                    viewModel.loadWallpaper()



                } else {
                    Toast.makeText(
                        requireContext(),
                        "Password must be at least 6 characters",
                        Toast.LENGTH_SHORT,
                    ).show()
                }

            }
        }
    }


}