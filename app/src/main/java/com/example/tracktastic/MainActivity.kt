package com.example.tracktastic

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.example.tracktastic.databinding.ActivityMainBinding
import com.example.tracktastic.ui.viemodels.HomepageViewModel
import com.example.tracktastic.ui.viemodels.LoginViewModel
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val CHANNEL_ID = "channel_id"
    private val notificationId = 101

    private val homepageViewModel: HomepageViewModel by viewModels()
    private val viewModel: LoginViewModel by viewModels()

    companion object {
        val HOME_ITEM = R.id.homeFragment
        val SET_NEW_ITEM = R.id.setNewFragment
        val STATISTICS_ITEM = R.id.statisticsFragment
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        createNotificationChannel()
        checkNotificationPermission()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationItems = mutableListOf(
            CurvedBottomNavigation.Model(HOME_ITEM, "Home", R.drawable.baseline_home_filled_24),
            CurvedBottomNavigation.Model(SET_NEW_ITEM, "SetNew", R.drawable.baseline_add_24),
            CurvedBottomNavigation.Model(
                STATISTICS_ITEM,
                "Statistics",
                R.drawable.baseline_bar_chart_24
            ),
        )

        binding.bottomNavigation.apply {
            bottomNavigationItems.forEach { add(it) }
            setOnClickMenuListener {
                navController.navigate(it.id)
            }
            show(HOME_ITEM)
            // optional
            setupNavController(navController)
        }
        navController.addOnDestinationChangedListener { navController: NavController, navDestination: NavDestination, bundle: Bundle? ->

            if (navController.currentDestination!!.id == R.id.loginFragment ||
                navController.currentDestination!!.id == R.id.splashScreenFragment ||
                navController.currentDestination!!.id == R.id.registerFragment ||
                navController.currentDestination!!.id == R.id.forgotPasswordFragment
            ) {
                binding.bottomNavigation.visibility = View.GONE
            } else {
                binding.bottomNavigation.visibility = View.VISIBLE
            }
        }
        //adding dialog on backpress if user wants to leave the app
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.currentDestination?.id == R.id.homeFragment ||
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
                    when (navController.currentDestination!!.id) {
                        SET_NEW_ITEM -> {
                            navController.popBackStack(R.id.homeFragment, false)
                        }

                        STATISTICS_ITEM -> {
                            navController.popBackStack(R.id.homeFragment, false)
                        }

                        else -> {
                            navController.navigateUp()
                        }
                    }
                }
            }
        })

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
        homepageViewModel.timerLogic.notification.observe(this) {

            if (!homepageViewModel.timerLogic.notification.value.isNullOrEmpty()) {
                sendNotification(
                    "wow",
                    homepageViewModel.timerLogic.notification.value.toString(),
                    R.drawable.banner
                )
            }
        }

        viewModel.notificationPermission.observe(this) {
            if (it != true) {
                cancelNotification()
            }
        }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission is already granted
                }

                else -> {
                    // Request the permission
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // sendNotification()
        } else {
            // Handle permission denial
        }
    }

    fun sendNotification(title: String, description: String, picture: Int) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val bitmap = BitmapFactory.decodeResource(applicationContext.resources, picture)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.tracktastic_5)
            .setContentTitle(title)
            .setContentText(description)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)) {
            checkNotificationPermission()
            notify(notificationId, builder.build())
        }
    }

    private fun cancelNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }
}


