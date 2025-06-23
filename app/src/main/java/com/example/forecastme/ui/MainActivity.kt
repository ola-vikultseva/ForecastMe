package com.example.forecastme.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.forecastme.R
import com.example.forecastme.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        with(binding) {
            navController.addOnDestinationChangedListener { _, destination, _ ->
                bottomNavigationView.isVisible = when (destination.id) {
                    R.id.current_weather_fragment,
                    R.id.future_weather_fragment,
                    R.id.settings_fragment -> true
                    else -> false
                }
            }
            setSupportActionBar(toolbar)
            toolbar.setupWithNavController(
                navController = navController,
                configuration = AppBarConfiguration(
                    setOf(
                        R.id.current_weather_fragment,
                        R.id.future_weather_fragment,
                        R.id.settings_fragment
                    )
                )
            )
            bottomNavigationView.setupWithNavController(navController)
        }
    }
}
