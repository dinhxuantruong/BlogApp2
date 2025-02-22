package com.example.blogapp.utils.network

import com.example.blogapp.api.myApi
import com.example.blogapp.utils.network.Constance.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        // Thêm timeout cho OkHttpClient
        .callTimeout(30, TimeUnit.SECONDS) // Thời gian tối đa để hoàn thành cuộc gọi
        .connectTimeout(5, TimeUnit.SECONDS) // Thời gian tối đa để kết nối với máy chủ
        .readTimeout(30, TimeUnit.SECONDS) // Thời gian tối đa để đọc dữ liệu từ máy chủ

        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val appApi: myApi by lazy {
        retrofit.create(myApi::class.java)
    }

}