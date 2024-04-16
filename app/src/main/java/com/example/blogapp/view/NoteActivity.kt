package com.example.blogapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.blogapp.R
import com.example.blogapp.databinding.ActivityNoteBinding
import com.example.blogapp.utils.SharedPreference.SharedPreferencesHelper
import com.example.blogapp.view.admin.AdminActivity

class NoteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNoteBinding
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferencesHelper = SharedPreferencesHelper(this)

        binding.edittext.setText("")
        val text = intent.getStringExtra("text")
        if (text!=null){
            binding.edittext.setText(text)
        }
        binding.edittext.setStylesBar(binding.stylesbar)
        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            sharedPreferencesHelper.saveLogDes(binding.edittext.text.toString())
            startActivity(intent)
            finish()
        }
    }
}