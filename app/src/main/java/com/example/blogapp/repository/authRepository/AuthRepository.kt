package com.example.blogapp.repository.authRepository

import android.content.Context
import com.example.blogapp.model.AcceptOTP
import com.example.blogapp.model.Auth
import com.example.blogapp.model.ForgetPass
import com.example.blogapp.model.Register
import com.example.blogapp.model.bAddComment
import com.example.blogapp.model.response.Blogs
import com.example.blogapp.model.response.Data
import com.example.blogapp.model.response.Profile
import com.example.blogapp.model.response.ResultMessage
import com.example.blogapp.model.response.Slide
import com.example.blogapp.model.response.User
import com.example.blogapp.model.response.comments
import com.example.blogapp.model.response.details
import com.example.blogapp.model.response.responLike
import com.example.blogapp.model.response.updatePr
import com.example.blogapp.model.sendOTP
import com.example.blogapp.utils.UserPreferences
import com.example.blogapp.utils.network.RetrofitInstance
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class AuthRepository(private val context: Context) {

    private val userPreferences = UserPreferences(context)


    suspend fun login(auth: Auth): Response<User> {
        return RetrofitInstance.appApi.authLogin(auth)
    }

    suspend fun saveAccessToken(token: String) {
        userPreferences.saveAuthToken(token)
    }


    suspend fun getUser(token: String): Response<Profile> {
        return RetrofitInstance.appApi.getUser(token)
    }

    suspend fun getBlogs(token: String): Response<Blogs> {
        return RetrofitInstance.appApi.getBlogs(token)
    }

    suspend fun getSlide(): Response<Slide> {
        return RetrofitInstance.appApi.addSlide()
    }

    suspend fun searchBlogs(token: String, title: String): Response<Blogs> {
        return RetrofitInstance.appApi.searchBlogs(token, title)
    }

    suspend fun detailsBlogs(token: String, id: String): Response<details> {
        return RetrofitInstance.appApi.detailsBlogs(token, id)
    }

    suspend fun addLike(token: String, id: String): Response<responLike> {
        return RetrofitInstance.appApi.addLike(token, id)
    }

    suspend fun getComment(token: String, id: String): Response<comments> {
        return RetrofitInstance.appApi.getComment(token, id)
    }

    suspend fun addComment(token: String, comment: bAddComment): Response<Data> {
        return RetrofitInstance.appApi.addComment(token, comment)
    }

    suspend fun getProfile(token: String): Response<Profile> {
        return RetrofitInstance.appApi.getUser(token)
    }

    suspend fun updateProfile(
        token: String,
        name: RequestBody,
        profession: RequestBody,
        profile_photo: MultipartBody.Part
    ): Response<updatePr> {
        return RetrofitInstance.appApi.updateProfile(token, name, profession, profile_photo)
    }

    suspend fun authLogout(token: String): Response<responLike> {
        return RetrofitInstance.appApi.authLogout(token)
    }

    suspend fun authRegister(register: Register): Response<ResultMessage> {
        return RetrofitInstance.appApi.authRegister(register)
    }

    suspend fun authAcceptRegister(acceptOTP: AcceptOTP): Response<ResultMessage> {
        return RetrofitInstance.appApi.authAcceptRegister(acceptOTP)
    }

    suspend fun authSendOtp(sendOTP: sendOTP): Response<ResultMessage> {
        return RetrofitInstance.appApi.authSendOTP(sendOTP)
    }

    suspend fun authCheckAccount(sendOTP: sendOTP): Response<ResultMessage> {
        return RetrofitInstance.appApi.authCheckAccountForget(sendOTP)
    }

    suspend fun authForgetPassword(forgetPass: ForgetPass): Response<ResultMessage> {
        return RetrofitInstance.appApi.authForgetPassword(forgetPass)
    }

    suspend fun addBlog(
        token: String,
        title: RequestBody,
        short_description: RequestBody,
        long_description: RequestBody,
        category_id: RequestBody,
        image: MultipartBody.Part
    ): Response<responLike> {
        return RetrofitInstance.appApi.addBlog(
            token,
            title,
            short_description,
            long_description,
            category_id,
            image
        )
    }

}