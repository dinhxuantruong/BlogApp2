package com.example.blogapp.model.response

data class Profile(
    val message :String,
    val email: String,
    val id: Int,
    val name: String,
    val profession: String,
    val profile_photo: String
)