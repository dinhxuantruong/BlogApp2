package com.example.blogapp.model.response

data class Data(
    val blog_id: Int,
    val created_at: String,
    val human_readable_createAt: String,
    val id: Int,
    val id_name: Int,
    val message: String,
    val name: String,
    val profile_photo: String,
    val updated_at: String,
    val user_id: Int
)