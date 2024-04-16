package com.example.blogapp.utils

sealed class Resources<out T>{
    data class Success<T>(val data: T) : Resources<T>()
    data class Error(val message: String) : Resources<Nothing>()
}
