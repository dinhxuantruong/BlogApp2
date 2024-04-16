package com.example.blogapp.model.response

data class updatePr(
    val email: String,
    val id: Int,
    val message: String,
    val name: String,
    val profession: String,
    val profile_photo: String
)