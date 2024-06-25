package com.example.tracktastic

import android.content.ContentValues.TAG
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.navigation.NavController
import androidx.navigation.NavDestination
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

        //supporting navigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        //adding navigation elements
        bottomNavigation.add(
            CurvedBottomNavigation.Model(1, "Home", R.drawable.ic_android_black_24dp)
        )
        bottomNavigation.add(
            CurvedBottomNavigation.Model(2, "SetNew", R.drawable.ic_android_black_24dp)
        )
        bottomNavigation.add(
            CurvedBottomNavigation.Model(3, "Settings", R.drawable.ic_android_black_24dp)
        )

        //hiding navigation from login/register screens
        navController.addOnDestinationChangedListener { navController: NavController, navDestination: NavDestination, bundle: Bundle? ->

            if (navController.currentDestination!!.id == R.id.loginFragment ||
                navController.currentDestination!!.id == R.id.splashScreenFragment ||
                navController.currentDestination!!.id == R.id.registerFragment ||
                navController.currentDestination!!.id == R.id.forgotPasswordFragment) {
                binding.bottomNavigation.visibility = View.GONE
            } else {
                binding.bottomNavigation.visibility = View.VISIBLE
            }
        }

        // adding functionality
        bottomNavigation.setOnClickMenuListener {
            when(it.id){
                1->{
                  //  navController.popBackStack(R.id.nav_graph, false)
                    navController.navigate(R.id.homeFragment)
                }
                2->{
                    //navController.popBackStack(R.id.nav_graph, false)
                    navController.navigate(R.id.setNewFragment)
                }
                3->{
                   //navController.popBackStack(R.id.nav_graph, false)
                    navController.navigate(R.id.settingsFragment)
                }
            }

        }

        //adding dialog on backpress if user wants to leave the app
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.currentDestination?.id == R.id.homeFragment ||
                    navController.currentDestination?.id == R.id.setNewFragment ||
                    navController.currentDestination?.id == R.id.settingsFragment ||
                    binding.bottomNavigation.visibility.equals(View.GONE)
                ) {
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("Are you sure you want to leave the app?")
                        .setPositiveButton("Yes") { _, _ ->
                            finish()
                        }
                        .setNegativeButton("No") { _, _ -> }
                        .show()
                } else {
                    navController.navigateUp()
                }
            }
        })



        //default bottom taqb
        bottomNavigation.show(1)


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