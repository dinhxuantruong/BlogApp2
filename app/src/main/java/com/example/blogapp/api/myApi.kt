package com.example.blogapp.api

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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface myApi {

    //Login
    @POST("login")
    suspend fun authLogin(@Body auth: Auth): Response<User>

    @POST("sendotp")
    suspend fun authSendOTP(@Body sendOTP: sendOTP): Response<ResultMessage>

    @POST("auth/register")
    suspend fun authRegister(@Body register: Register): Response<ResultMessage>

    @POST("auth/otpres")
    suspend fun authAcceptRegister(@Body acceptOTP: AcceptOTP): Response<ResultMessage>

    @POST("forgot")
    suspend fun authForgetPassword(@Body forgetPass: ForgetPass): Response<ResultMessage>

    @POST("check/account")
    suspend fun authCheckAccountForget(@Body sendOTP: sendOTP): Response<ResultMessage>

    @POST("logout")
    suspend fun authLogout(@Header("Authorization") token: String): Response<responLike>

    //get user
    @GET("read/user")
    suspend fun getUser(@Header("Authorization") token: String): Response<Profile>


    @GET("all/blog")
    suspend fun getBlogs(@Header("Authorization") token: String): Response<Blogs>


    @GET("allSlider")
    suspend fun addSlide(): Response<Slide>


    @GET("search/blog")
    suspend fun searchBlogs(
        @Header("Authorization") token: String,
        @Query("title") title: String
    ): Response<Blogs>


    @GET("details/{id}")
    suspend fun detailsBlogs(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<details>


    @POST("blogs/like/{id}")
    suspend fun addLike(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<responLike>

    @GET("list/comment/{id}")
    suspend fun getComment(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<comments>


    @POST("add/comment")
    suspend fun addComment(
        @Header("Authorization") token: String, @Body comment: bAddComment
    ): Response<Data>


    @Multipart
    @POST("user/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("profession") profession: RequestBody,
        @Part profile_photo: MultipartBody.Part
    ): Response<updatePr>


    @Multipart
    @POST("create/blog")
    suspend fun addBlog(
        @Header("Authorization") token: String,
        @Part("title") title: RequestBody,
        @Part("short_description") short_description: RequestBody,
        @Part("long_description") long_description: RequestBody,
        @Part("category_id") category_id: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<responLike>

}