package com.example.blogapp.view.Auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.blogapp.R
import com.example.blogapp.databinding.ActivityAuthBinding
import com.example.blogapp.databinding.ActivityMainBinding
import com.example.blogapp.repository.authRepository.AuthRepository
import com.example.blogapp.viewmodel.AuthViewModel
import com.example.blogapp.viewmodel.AuthViewModelFactory

class AuthActivity : AppCompatActivity() {
    private lateinit var viewModel: AuthViewModel
    private var _binding : ActivityAuthBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = AuthRepository(this)
        val vmFactory = AuthViewModelFactory(repository)
        viewModel = ViewModelProvider(this,vmFactory)[AuthViewModel::class.java]





    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}