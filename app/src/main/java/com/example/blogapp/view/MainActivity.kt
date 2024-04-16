package com.example.blogapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.blogapp.R
import com.example.blogapp.databinding.ActivityMainBinding
import com.example.blogapp.utils.UserPreferences

import com.example.blogapp.view.Auth.AuthActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private var  _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomBarNav : BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startActivity(Intent(this,AuthActivity::class.java))
        finish()

//        bottomBarNav = findViewById(R.id.butoomBar)
//        navController = findNavController(R.id.test)
//        appBarConfiguration = AppBarConfiguration(setOf(
//            R.id.action_blankFragment3_to_blankFragment1,
//            R.id.action_blankFragment1_to_blankFragment2,
//            R.id.action_blankFragment2_to_blankFragment3))
//
//        setupActionBarWithNavController(navController,appBarConfiguration)
//        bottomBarNav.setupWithNavController(navController)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}