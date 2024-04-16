package com.example.blogapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blogapp.model.response.Blogs
import com.example.blogapp.model.response.responLike
import com.example.blogapp.repository.authRepository.AuthRepository
import com.example.blogapp.utils.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException

class AddViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _resultAddBlog: MutableLiveData<Resources<responLike>> = MutableLiveData()
    val resultAddBlog: LiveData<Resources<responLike>> get() = _resultAddBlog
    private val _getBlogs: MutableLiveData<Resources<Blogs>> = MutableLiveData()
    val getBlogs: LiveData<Resources<Blogs>> get() = _getBlogs
    fun blogs(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getBlogs(token)
                if (response.isSuccessful) {
                    val blogs = response.body()!!
                    _getBlogs.postValue(Resources.Success(blogs))
                } else {
                    val errorMessage = "User unsuccessful: ${response.message()}"
                    _getBlogs.postValue(Resources.Error(errorMessage))
                }
            } catch (e: IOException) {
                _getBlogs.postValue(Resources.Error("Network connection error!"))
            } catch (e: HttpException) {
                _getBlogs.postValue(Resources.Error("Error HTTP: ${e.message}"))
            } catch (e: Exception) {
                _getBlogs.postValue(Resources.Error("An unknown error has occurred!"))
            }

        }
    }
    fun addBlog(
        token: String,
        title: RequestBody,
        short_description: RequestBody,
        long_description: RequestBody,
        category_id: RequestBody,
        image: MultipartBody.Part
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.addBlog(
                    token,
                    title,
                    short_description,
                    long_description,
                    category_id,
                    image
                )
                if (response.isSuccessful) {
                    val blog = response.body()!!
                    _resultAddBlog.postValue(Resources.Success(blog))
                } else {
                    val errorMessage = "Update profile unsuccessful: ${response.message()}"
                    _resultAddBlog.postValue(Resources.Error(errorMessage))
                }
            } catch (e: IOException) {
                _resultAddBlog.postValue(Resources.Error("Network connection error2!"))
            } catch (e: HttpException) {
                _resultAddBlog.postValue(Resources.Error("Error HTTP: ${e.message}"))
            } catch (e: Exception) {
                _resultAddBlog.postValue(Resources.Error("An unknown error has occurred!"))
            }
        }
    }


}