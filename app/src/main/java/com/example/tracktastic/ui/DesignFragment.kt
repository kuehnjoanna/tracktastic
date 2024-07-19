package com.example.tracktastic.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.tracktastic.databinding.FragmentDesignBinding
import com.example.tracktastic.ui.viemodels.SettingsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class DesignFragment : Fragment() {
    private lateinit var binding: FragmentDesignBinding

    private val viewModel: SettingsViewModel by activityViewModels()

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val reference: DatabaseReference = database.reference.child("users")
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    val storageRefrence: StorageReference = firebaseStorage.reference

    var imageURI: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDesignBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registeractivityforresult()
        viewModel.loadFeatherWallpaper()

        viewModel.repository.wallpaper1.observe(viewLifecycleOwner) {
            binding.ivNewWallpaper1.load(viewModel.repository.wallpaper1.value!![0].largeImageURL)
        }
        viewModel.repository.wallpaper2.observe(viewLifecycleOwner) {
            binding.ivNewWallpaper2.load(viewModel.repository.wallpaper2.value!![0].largeImageURL)
        }
        viewModel.repository.wallpaper3.observe(viewLifecycleOwner) {
            binding.ivNewWallpaper4.load(viewModel.repository.wallpaper3.value!![0].largeImageURL)
        }

        binding.ivNewWallpaper1.setOnClickListener {
            lifecycleScope.launch {
                viewModel.repository.upload(
                    viewModel.repository.wallpaper1.value!![0].largeImageURL.toString(),
                    "wallpaperUrl"
                ) {
                    if (it != null) {
                        Log.d(
                            "upload feather function",
                            "success, "
                        )
                    } else {
                        Log.d("upload feather function", "fail, ")
                    }

                }
            }

        }
        viewModel.repository.wallpaper2.observe(viewLifecycleOwner) {
            binding.ivNewWallpaper1.load(viewModel.repository.wallpaper1.value!![0].largeImageURL)
        }

        binding.ivNewWallpaper2.setOnClickListener {
            lifecycleScope.launch {
                viewModel.repository.upload(
                    viewModel.repository.wallpaper2.value!![0].largeImageURL.toString(),
                    "wallpaperUrl"
                ) {
                    if (it != null) {
                        Log.d(
                            "upload feather function",
                            "success, "
                        )
                    } else {
                        Log.d("upload feather function", "fail, ")
                    }

                }
            }

        }
        viewModel.repository.wallpaper3.observe(viewLifecycleOwner) {
            binding.ivNewWallpaper1.load(viewModel.repository.wallpaper1.value!![0].largeImageURL)
        }

        binding.ivNewWallpaper4.setOnClickListener {
            lifecycleScope.launch {
                viewModel.repository.upload(
                    viewModel.repository.wallpaper3.value!![0].largeImageURL.toString(),
                    "wallpaperUrl"
                ) {
                    if (it != null) {
                        Log.d(
                            "upload feather function",
                            "success, "
                        )
                    } else {
                        Log.d("upload feather function", "fail, ")
                    }

                }
            }

        }
        binding.btnLoadOwnPicture.setOnClickListener {
            chooseImage()
        }
        binding.btnSaveChanges.setOnClickListener {
            upload()
        }
    }

    fun registeractivityforresult() {
        activityResultLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback { result ->
                    val resultCode = result.resultCode
                    val imageData = result.data
                    if (resultCode == Activity.RESULT_OK && imageData != null) {
                        imageURI = imageData.data
                        //picasso
                        //  Picasso.get().load(imageURI)

                        imageURI.let {
                            Picasso.get().load(it).into(binding.ivNewWallpaper5)
                        }
                    }

                })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            //start activityresultlaauncher
            activityResultLauncher.launch(intent)
        }
    }

    fun chooseImage() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), 1)
        } else {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            //start activityresultlaauncher
            activityResultLauncher.launch(intent)

        }
    }


    fun upload() {


        //uuuid

        val imageName = FirebaseAuth.getInstance().currentUser?.uid + "wallpaperUrl"
        val imageReference = storageRefrence.child("images").child(imageName)
        // imageReference.putFile(imageURI)
        imageURI?.let { uri ->
            imageReference.putFile(uri).addOnSuccessListener {

                Toast.makeText(requireContext(), "Image uploaded", Toast.LENGTH_SHORT).show()

                //downloadable url
                val myUploadedImage = storageRefrence.child("images").child(imageName)

                myUploadedImage.downloadUrl.addOnSuccessListener { url ->
                    val imageUrl = url.toString()
                    FirebaseFirestore.getInstance().collection("users")
                        .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
                        .update("wallpaperUrl", imageUrl)
                }

            }.addOnFailureListener {

                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }


}