package com.example.skillcinema.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI
import com.example.skillcinema.R
import com.example.skillcinema.databinding.ActivityAppBinding
import com.example.skillcinema.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppBinding
    private var currentNavController: LiveData<NavController>? = null
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        bottomNavigationView = binding.navView
        setContentView(binding.root)
        Log.d("HomeFragments", "App onCreate")
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.nav_home
            setupBottomNavigationBar()
        }

        val sharedPref = getSharedPreferences("isFirstTime", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(KEY_IS_FIRST, savedInstanceState == null)
        editor.apply()

    }

    private fun setupBottomNavigationBar() {

        val navGraphIds = listOf(R.navigation.nav_home, R.navigation.nav_search, R.navigation.nav_profile)

        // setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )
        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this) { navController ->
            NavigationUI.setupWithNavController(binding.toolbar, navController)
        }

        currentNavController = controller

    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false || super.onSupportNavigateUp()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d("HomeFragments", "App onRestoreInstanceState")
        // now that BottomNavigationBar has resoted its instance state
        setupBottomNavigationBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("HomeFragments", "App onDestroy")
    }


    companion object {
        const val KEY_IS_FIRST = "firstTime"
    }

}