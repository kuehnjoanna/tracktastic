package com.example.tracktastic

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import com.example.tracktastic.ui.viemodels.LoginViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //falls ein fehler/exception kommt, wird er direkt als toast angezeigt
        viewModel.info.observe(this) {

            if (!viewModel.info.value.isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    viewModel.info.value.toString(),
                    Toast.LENGTH_LONG,
                ).show()
                Log.d(TAG, viewModel.info.value.toString())
            }
        }
    }
}