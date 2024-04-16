package com.example.blogapp.view.Home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.blogapp.R
import com.example.blogapp.databinding.ActivityHomeBinding
import com.example.blogapp.repository.authRepository.AuthRepository
import com.example.blogapp.viewmodel.HomeViewModel
import com.example.blogapp.viewmodel.HomeViewModelFactory
import okhttp3.internal.notify

class HomeActivity : AppCompatActivity() {
    private var _binding : ActivityHomeBinding? = null
    private val binding get() =  _binding!!
    private lateinit var viewModel : HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val repository = AuthRepository(this)
        val vmFactory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(this,vmFactory)[HomeViewModel::class.java]

        replaceFragment(HomeFragment())
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragment())
                R.id.search -> replaceFragment(SearchFragment())
                R.id.setting-> replaceFragment(SettingFragment())

                else -> {

                }
            }
            true
        }



    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}