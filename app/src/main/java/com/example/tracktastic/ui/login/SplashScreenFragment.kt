package com.example.tracktastic.ui.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.tracktastic.R
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
        /*
        val mediaControl = MediaController(requireContext())
        mediaControl.setAnchorView(binding.videoView)
        //  val videoPath = "android.resource://raw/${R.raw.splash_video}"
        //  val videoPath = "android.resource://" + getPa "/${R.raw.splash}"
        //  val videoUri = Uri.parse(videoPath)
        //val videoUri = Uri.parse("https://cdn.pixabay.com/video/2024/05/25/213616_large.mp4")
        binding.videoView.setMediaController(mediaControl)
        // binding.videoView.setVideoURI(videoUri)
        val videoPath =
            "android.resource://" + "com.example.tracktastic.ui.login" + "/" + R.raw.splash_video
        val uri = Uri.parse(videoPath)
        binding.videoView.setVideoURI(uri)
        binding.videoView.start()

         */

        Glide.with(this).asGif().load(R.raw.splash).listener(object : RequestListener<GifDrawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<GifDrawable>?,
                isFirstResource: Boolean
            ): Boolean {
                // Handle the error
                return false
            }

            override fun onResourceReady(
                resource: GifDrawable?,
                model: Any?,
                target: Target<GifDrawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                // Set the GIF to play only once
                resource?.setLoopCount(1)
                // Get the duration of the GIF
                val duration = 8000

                // Use a Handler to navigate after the GIF has played
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.root.alpha = 1f

                    binding.root.animate().setDuration(2500).alpha(1f)
                    findNavController().navigate(R.id.loginFragment)
                }, duration.toLong())
                return false
            }

        }).into(binding.img)


    }
}