package com.example.tracktastic.ui.login

import android.content.ContentValues.TAG
import android.os.Bundle
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
import com.example.tracktastic.databinding.FragmentLoginBinding
import com.example.tracktastic.ui.viemodels.LoginViewModel


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //current user observer um zu blick halten wenn er sich andert und direkt reagieren
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->

            if (user == null) {
                //User ist nicht eingeloggt
                Log.d("CurrentUser", "Kein User eingeloggt")
            } else {
                //User ist eingeloggt
                // viewModel.loadWallpaper()
                Log.d("CurrentUser", user.uid)
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
            }

            //navigieren zum forgot password fragment
            binding.BTNForgotPasswordLogin.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
            }

            //navigieren zu register fragment
            binding.TVRedirectToRegister.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }

            //einloggen into Firebase wenn password oder email feflder NICHT leer sind
            binding.BTNLoginLogin.setOnClickListener {

                Log.d(TAG, "Login Button works, ${viewModel.currentUser}")
                val email = binding.ETuserEmailLogin.text.toString()
                val password = binding.ETuserPasswordLogin.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.signInWithFirebase(email, password)


                }
            }
        }
    }
}