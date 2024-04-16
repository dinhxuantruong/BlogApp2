package com.example.blogapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blogapp.model.bAddComment
import com.example.blogapp.model.response.Data
import com.example.blogapp.model.response.comments
import com.example.blogapp.model.response.responLike
import com.example.blogapp.repository.authRepository.AuthRepository
import com.example.blogapp.utils.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class CommentViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _resultComment : MutableLiveData<Resources<comments>> = MutableLiveData()
    val resultComments : LiveData<Resources<comments>> get() =  _resultComment

    private val _resultAddComment : MutableLiveData<Resources<Data>> = MutableLiveData()
    val resultAddComment : LiveData<Resources<Data>> get() =  _resultAddComment

    fun addComment(token : String, comment: bAddComment) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.addComment(token, comment)
                if (response.isSuccessful) {
                    val comment = response.body()!!
                    _resultAddComment.postValue(Resources.Success(comment))
                } else {
                    val errorMessage = "Add comment unsuccessful: ${response.message()}"
                    _resultAddComment.postValue(Resources.Error(errorMessage))
                }
            } catch (e: IOException) {
                _resultAddComment.postValue(Resources.Error("Network connection error!"))
            } catch (e: HttpException) {
                _resultAddComment.postValue(Resources.Error("Error HTTP: ${e.message}"))
            } catch (e: Exception) {
                _resultAddComment.postValue(Resources.Error("An unknown error has occurred!"))
            }
        }
    }

    fun getComment(token: String, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getComment(token, id)
                if (response.isSuccessful) {
                    val comment = response.body()!!
                    _resultComment.postValue(Resources.Success(comment))
                } else {
                    val errorMessage = "Comment unsuccessful: ${response.message()}"
                    _resultComment.postValue(Resources.Error(errorMessage))
                }
            } catch (e: IOException) {
                _resultComment.postValue(Resources.Error("Network connection error!"))
            } catch (e: HttpException) {
                _resultComment.postValue(Resources.Error("Error HTTP: ${e.message}"))
            } catch (e: Exception) {
                _resultComment.postValue(Resources.Error("An unknown error has occurred!"))
            }
        }
    }
}