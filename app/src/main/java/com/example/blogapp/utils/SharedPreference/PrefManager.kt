package com.example.app.SharedPreference

import android.content.Context
import android.content.SharedPreferences

class PrefManager(var context: Context) {
    val PRIVATE_MODE = 0

    //SharedPreference file name
    private val PREF_NAME = "SharedPreference"

    private val IS_LOGIN = "is_login"

    var preferen: SharedPreferences? = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE)
    var editor : SharedPreferences.Editor? = preferen?.edit()

    fun setLogin(isLogin : Boolean){
        editor?.putBoolean(IS_LOGIN,isLogin)
        editor?.commit()
    }

    fun saveCredentials(email: String, password: String) {
        editor?.putString("email", email)
        editor?.putString("password", password)
        editor?.commit()
    }

//    fun setUserName(username : String?){
//        editor?.putString("username",username)
//        editor?.commit()
//    }

    fun isLogin():Boolean ?{
        return preferen?.getBoolean(IS_LOGIN,false)
    }

    fun getUserName() : String ?{
        return preferen?.getString("username","")
    }
    fun getPassword() : String? {
        return preferen?.getString("password","")
    }

    fun removeDate(){
        editor?.clear()
        editor?.commit()
    }
}