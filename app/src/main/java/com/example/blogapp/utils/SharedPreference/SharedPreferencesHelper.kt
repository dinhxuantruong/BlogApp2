package com.example.blogapp.utils.SharedPreference

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri

class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun saveTitle(title: String) {
        val editor = sharedPreferences.edit()
        editor.putString("titleKey", title)
        editor.apply()
    }

    fun saveImage(image: Uri?) {
        val editor = sharedPreferences.edit()
        if (image != null) {
            editor.putString("imageUri", image.toString()) // Chuyển Uri thành chuỗi và lưu vào SharedPreferences
        } else {
            // Nếu Uri null, có thể xóa khỏi SharedPreferences hoặc xử lý tùy thuộc vào logic của bạn
            editor.remove("imageUri")
        }
        editor.apply()
    }


    fun getImage(): Uri? {
        val imageUriString = sharedPreferences.getString("imageUri", null)
        return if (imageUriString != null) Uri.parse(imageUriString) else null
    }



    fun saveLogDes(title: String) {
        val editor = sharedPreferences.edit()
        editor.putString("logKey", title)
        editor.apply()
    }

    fun getTitle(): String? {
        return sharedPreferences.getString("titleKey", null)
    }

    fun saveShortDescription(shortDes: String) {
        val editor = sharedPreferences.edit()
        editor.putString("shortDesKey", shortDes)
        editor.apply()
    }

    fun getShortDescription(): String? {
        return sharedPreferences.getString("shortDesKey", null)
    }
    fun getLongDescription(): String? {
        return sharedPreferences.getString("logKey", null)
    }
    fun clearData() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

}
