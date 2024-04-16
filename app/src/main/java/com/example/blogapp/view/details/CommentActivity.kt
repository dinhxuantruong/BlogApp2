package com.example.blogapp.view.details

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.blogapp.R
import com.example.blogapp.adapter.commentAdapter
import com.example.blogapp.adapter.listBlogsAdapter
import com.example.blogapp.databinding.ActivityCommentBinding
import com.example.blogapp.model.bAddComment
import com.example.blogapp.model.response.Data
import com.example.blogapp.repository.authRepository.AuthRepository
import com.example.blogapp.utils.Resources
import com.example.blogapp.utils.UserPreferences
import com.example.blogapp.view.Home.HomeActivity
import com.example.blogapp.viewmodel.CommentViewModel
import com.example.blogapp.viewmodel.CommentViewModelFactory


class CommentActivity : AppCompatActivity() {
    private var _binding : ActivityCommentBinding? = null
    private val binding get() =  _binding!!
    private lateinit var viewModel : CommentViewModel
    private lateinit var adapter: commentAdapter
    private lateinit var listComment : MutableList<Data>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)



        setupUI()
        setupViewModel()
        observeData()


    }

    private fun observeData() {
        viewModel.resultComments.observe(this){
            when(it){
                is Resources.Success -> {
                    listComment = it.data.data.toMutableList()
                    resetView()
                }

                is Resources.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.resultAddComment.observe(this){
            when(it){
                is Resources.Success -> {
                    listComment.add(0,it.data)
                    resetView()
                }

                is Resources.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun resetView(){
        adapter = commentAdapter(this,listComment)
        binding.toolRegister.title = "${listComment.size.toString()} Comment"
        binding.listView.adapter = adapter
    }

    private fun setupViewModel() {
        listComment = mutableListOf()
        val repository = AuthRepository(this)
        val vmFactory = CommentViewModelFactory(repository)
        viewModel = ViewModelProvider(this,vmFactory)[CommentViewModel::class.java]
    }

    private fun setupUI() {
        binding.toolRegister.setNavigationOnClickListener {
           // startActivity(Intent(this, DetailsActivity::class.java))
            finish()
        }
        val id = intent.getStringExtra("id")
        id?.let { blogId ->
            val userPreferences = UserPreferences(this)
            userPreferences.authToken.asLiveData().observe(this, Observer { token ->
                viewModel.getComment(token.toString(), blogId)
                binding.btnSend.setOnClickListener {
                    val newComment = binding.editTextMessage.text.toString()
                    viewModel.addComment(token.toString(), bAddComment(id,newComment) )
                    binding.editTextMessage.text = null
                    binding.editTextMessage.clearFocus()
                    hideKeyboard(binding.editTextMessage)
                }
            })
        }
    }

  private  fun Activity.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }




    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}