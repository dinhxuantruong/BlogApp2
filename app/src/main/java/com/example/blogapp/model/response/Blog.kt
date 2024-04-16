package com.example.blogapp.model.response

data class Blog(
    val category_id: Int,
    val comments_count: Int,
    val bloglikes_count : Int,
    val created_at: String,
    val human_readable_createAt: String,
    val id: Int,
    val image_url: String,
    val liked_by_current_user: Boolean,
    val long_description: String,
    val short_description: String,
    val title: String,
    val updated_at: String,
    val user_id: Int
)