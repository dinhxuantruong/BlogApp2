package com.example.blogapp.model.response

import android.provider.ContactsContract.CommonDataKinds.Email

data class ResultMessage(
    val message: String,
    val email: String,
)