package com.example.blogapp.model.response

data class responseUser(
    val access_token: String,
    val email: String,
    val id: Int,
    val name: String,
    val profession: String,
    val profile_photo: String,
    val refresh_token: String,
    val role : Int,
)