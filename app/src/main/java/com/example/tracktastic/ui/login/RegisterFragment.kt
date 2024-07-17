package com.example.tracktastic.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tracktastic.R
import com.example.tracktastic.databinding.FragmentRegisterBinding
import com.example.tracktastic.ui.viemodels.LoginViewModel

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

                if (viewModel.isUserVerified() == true) {
                    //User ist eingeloggt
                    // viewModel.loadWallpaper()
                    Log.d("CurrentUser", user.uid)
                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToHomeFragment())
                } else {

                }
            }

            binding.loginRedirectText.setOnClickListener {
                findNavController().navigate(R.id.loginFragment)

            }






            binding.BTNcreateNewUser.setOnClickListener {

                val userName = binding.ETuserNameNew.text.toString()
                val userEmail = binding.ETuserEmailNew.text.toString()
                val userPassword = binding.ETuserPasswordNew.text.toString()
                //creating new account if password requirements are met
                if (viewModel.isValidPassword(userPassword) == true) {
                    viewModel.signUpWithFirebase(userEmail, userPassword, userName)

                    findNavController().navigate(R.id.loginFragment)


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