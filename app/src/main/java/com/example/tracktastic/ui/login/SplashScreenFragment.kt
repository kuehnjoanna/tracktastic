package com.example.tracktastic.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tracktastic.databinding.FragmentSplashScreenBinding

private lateinit var binding: FragmentSplashScreenBinding

class SplashScreenFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mediaControl = MediaController(requireContext())
        mediaControl.setAnchorView(binding.videoView)
        //  val videoPath = "android.resource://raw/${R.raw.splash_video}"
        //  val videoPath = "android.resource://" + getPa "/${R.raw.splash}"
        //  val videoUri = Uri.parse(videoPath)
        //val videoUri = Uri.parse("https://cdn.pixabay.com/video/2024/05/25/213616_large.mp4")
        binding.videoView.setMediaController(mediaControl)
        // binding.videoView.setVideoURI(videoUri)
        //  binding.videoView.setVideoURI(videoUri)
        binding.videoView.start()
        binding.videoView.setOnCompletionListener {
            findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment())

        }
        Thread.sleep(500)
        findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment())

    }
}