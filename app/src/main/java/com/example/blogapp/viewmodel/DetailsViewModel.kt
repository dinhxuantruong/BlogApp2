package com.example.blogapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blogapp.model.response.details
import com.example.blogapp.model.response.responLike
import com.example.blogapp.repository.authRepository.AuthRepository
import com.example.blogapp.utils.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class DetailsViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _resultDetails: MutableLiveData<Resources<details>> = MutableLiveData()
    val resultDetails: LiveData<Resources<details>> get() = _resultDetails

    private val _resultBlogLike: MutableLiveData<Resources<responLike>> = MutableLiveData()
    val resultBlogLike: LiveData<Resources<responLike>> get() = _resultBlogLike


    fun addLike(token: String, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.addLike(token, id)
                if (response.isSuccessful) {
                    val like = response.body()!!
                    _resultBlogLike.postValue(Resources.Success(like))
                } else {
                    val errorMessage = "Like unsuccessful: ${response.message()}"
                    _resultBlogLike.postValue(Resources.Error(errorMessage))
                }
            } catch (e: IOException) {
                _resultBlogLike.postValue(Resources.Error("Network connection error!"))
            } catch (e: HttpException) {
                _resultBlogLike.postValue(Resources.Error("Error HTTP: ${e.message}"))
            } catch (e: Exception) {
                _resultBlogLike.postValue(Resources.Error("An unknown error has occurred!"))
            }
        }
    }

    fun detailsBlog(token: String, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.detailsBlogs(token, id)
                if (response.isSuccessful) {
                    val details = response.body()!!
                    _resultDetails.postValue(Resources.Success(details))
                } else {
                    val errorMessage = "Details unsuccessful: ${response.message()}"
                    _resultDetails.postValue(Resources.Error(errorMessage))
                }
            } catch (e: IOException) {
                _resultDetails.postValue(Resources.Error("Network connection error!"))
            } catch (e: HttpException) {
                _resultDetails.postValue(Resources.Error("Error HTTP: ${e.message}"))
            } catch (e: Exception) {
                _resultDetails.postValue(Resources.Error("An unknown error has occurred!"))
            }
        }
    }
}