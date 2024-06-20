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
import androidx.navigation.fragment.NavHostFragment
import com.example.tracktastic.databinding.ActivityMainBinding
import com.example.tracktastic.ui.HomeFragment
import com.example.tracktastic.ui.SetNewFragment
import com.example.tracktastic.ui.SettingsFragment
import com.example.tracktastic.ui.viemodels.LoginViewModel
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigation = binding.bottomNavigation

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

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
                    navController.navigate(R.id.homeFragment)
                }
                2->{
                    navController.navigate(R.id.setNewFragment)
                }
                3->{
                    navController.navigate(R.id.settingsFragment)
                }
            }
        }



        //default bottom taqb

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