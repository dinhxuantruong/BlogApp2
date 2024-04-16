package com.example.blogapp.view.details

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.blogapp.R
import com.example.blogapp.databinding.ActivityDetailsBinding
import com.example.blogapp.model.response.Blog
import com.example.blogapp.repository.authRepository.AuthRepository
import com.example.blogapp.utils.Resources
import com.example.blogapp.utils.UserPreferences
import com.example.blogapp.view.Home.HomeActivity
import com.example.blogapp.viewmodel.DetailsViewModel
import com.example.blogapp.viewmodel.DetailsViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso


class DetailsActivity : AppCompatActivity() {
    private var _binding: ActivityDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupUI()
        setupViewModel()
        observeData()
    }


    private fun setupUI() {
        binding.toolRegister.setNavigationOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }


        val id = intent.getStringExtra("id")
        if (intent.extras?.getBoolean("check") == true) {
            // Handle check if needed
        }
        binding.btnComment.setOnClickListener {
            val intent = Intent(this,CommentActivity::class.java)
            intent.putExtra("id",id.toString())
            startActivity(intent)
        }


        id?.let { blogId ->
            val userPreferences = UserPreferences(this)
            userPreferences.authToken.asLiveData().observe(this, Observer { token ->
                viewModel.detailsBlog(token.toString(), blogId)
                binding.btnLike.setOnClickListener {
                    viewModel.addLike(token.toString(), blogId)
                }
            })
        }
    }

    private fun setupViewModel() {
        val repository = AuthRepository(this)
        val vmFactory = DetailsViewModelFactory(repository)
        viewModel = ViewModelProvider(this, vmFactory)[DetailsViewModel::class.java]
    }

    private fun observeData() {
        viewModel.resultBlogLike.observe(this) { result ->
            when (result) {
                is Resources.Success -> {
                    val likeCount = binding.plusLike.text.toString().toInt()
                    binding.plusLike.text =
                        (likeCount + if (result.data.status) 1 else -1).toString()
                    binding.btnLike.setImageResource(if (result.data.status) R.drawable.like else R.drawable.unlike)
                }

                is Resources.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }

        viewModel.resultDetails.observe(this) { result ->
            when (result) {
                is Resources.Success -> {
                    val blog = result.data.blogs
                    binding.btnLike.setImageResource(if (blog.liked_by_current_user) R.drawable.like else R.drawable.unlike)
                    binding.plusLike.text = formatNumber(blog.bloglikes_count)
                    updateUI(blog)
                }

                is Resources.Error -> {
                    // Handle error
                }
            }
        }
    }

    private fun updateUI(blog: Blog) {
        with(binding) {
            Picasso.get().load(blog.image_url).into(imageView2)
            txtTime.text = blog.human_readable_createAt
            txtTitle.text = blog.title
            txtBody.text = blog.long_description
        }
    }

    private fun formatNumber(number: Int): String {
        val suffixes = arrayOf("", "k", "M", "B", "T")
        var value = number
        var index = 0
        while (value >= 1000 && index < suffixes.size - 1) {
            value /= 1000
            index++
        }
        return "$value${suffixes[index]}"
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
