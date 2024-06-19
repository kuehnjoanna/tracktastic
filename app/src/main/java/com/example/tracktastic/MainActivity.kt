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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import com.example.tracktastic.ui.HomeFragment
import com.example.tracktastic.ui.SetNewFragment
import com.example.tracktastic.ui.SettingsFragment
import com.example.tracktastic.ui.viemodels.LoginViewModel
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation

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
        val bottomNavigation = findViewById<CurvedBottomNavigation>(R.id.bottomNavigation)

        bottomNavigation.add(
            CurvedBottomNavigation.Model(1, "Home", R.drawable.ic_android_black_24dp)
        )
        bottomNavigation.add(
            CurvedBottomNavigation.Model(2, "SetNew", R.drawable.ic_android_black_24dp)
        )
        bottomNavigation.add(
            CurvedBottomNavigation.Model(3, "Settings", R.drawable.ic_android_black_24dp)
        )





        bottomNavigation.setOnClickMenuListener {
            when(it.id){
                1->{
                    replaceFragment(HomeFragment())
                }
                2->{
                    replaceFragment(SetNewFragment())
                }
                3->{
                    replaceFragment(SettingsFragment())
                }
            }
        }


        //default bottom taqb

        replaceFragment(HomeFragment())
        bottomNavigation.show(2)
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
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navHostFragment, fragment)
            .commit()
    }
}