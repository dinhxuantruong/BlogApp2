package com.example.blogapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blogapp.model.response.Blogs
import com.example.blogapp.model.response.Profile
import com.example.blogapp.model.response.Slide
import com.example.blogapp.model.response.responLike
import com.example.blogapp.model.response.updatePr
import com.example.blogapp.repository.authRepository.AuthRepository
import com.example.blogapp.utils.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException

class HomeViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _getUser: MutableLiveData<Resources<Profile>> = MutableLiveData()
    val getUser: LiveData<Resources<Profile>> get() = _getUser

    private val _getBlogs: MutableLiveData<Resources<Blogs>> = MutableLiveData()
    val getBlogs: LiveData<Resources<Blogs>> get() = _getBlogs

    private val _getSlide: MutableLiveData<Resources<Slide>> = MutableLiveData()
    val getSlide: LiveData<Resources<Slide>> get() = _getSlide


    private val _resultProfile : MutableLiveData<Resources<Profile>> = MutableLiveData()
    val resultProfile : LiveData<Resources<Profile>> get() =  _resultProfile

    private val _resultUpdateProfile : MutableLiveData<Resources<updatePr>> = MutableLiveData()
    val resultUpdateProfile : LiveData<Resources<updatePr>> get() =  _resultUpdateProfile

    private val _resultLogout : MutableLiveData<Resources<responLike>>  = MutableLiveData()
    val resultLogout : LiveData<Resources<responLike>> get() = _resultLogout

    fun authLogout(token : String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.authLogout(token)
                if (response.isSuccessful) {
                    val logout = response.body()!!
                    _resultLogout.postValue(Resources.Success(logout))
                } else {
                    val errorMessage = "Logout unsuccessful: ${response.message()}"
                    _resultLogout.postValue(Resources.Error(errorMessage))
                }
            } catch (e: IOException) {
                _resultLogout.postValue(Resources.Error("Network connection error2!"))
            } catch (e: HttpException) {
                _resultLogout.postValue(Resources.Error("Error HTTP: ${e.message}"))
            } catch (e: Exception) {
                _resultLogout.postValue(Resources.Error("An unknown error has occurred!"))
            }
        }
    }

    fun updateProfile(
        token: String,
        name: RequestBody,
        profession: RequestBody,
        profile_photo: MultipartBody.Part)  {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.e("MAIN",profession.toString())
                val response = repository.updateProfile(token,name,profession,profile_photo)
                if (response.isSuccessful) {
                    val update = response.body()!!
                    _resultUpdateProfile.postValue(Resources.Success(update))
                } else {
                    val errorMessage = "Update profile unsuccessful: ${response.message()}"
                    _resultUpdateProfile.postValue(Resources.Error(errorMessage))
                }
            } catch (e: IOException) {
                _resultUpdateProfile.postValue(Resources.Error("Network connection error2!"))
            } catch (e: HttpException) {
                _resultUpdateProfile.postValue(Resources.Error("Error HTTP: ${e.message}"))
            } catch (e: Exception) {
                _resultUpdateProfile.postValue(Resources.Error("An unknown error has occurred!"))
            }
        }
    }

    fun getProfile(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getProfile(token)
                if (response.isSuccessful) {
                    val user = response.body()!!
                    _resultProfile.postValue(Resources.Success(user))
                } else {
                    val errorMessage = "Profile unsuccessful: ${response.message()}"
                    _resultProfile.postValue(Resources.Error(errorMessage))
                }
            } catch (e: IOException) {
                _resultProfile.postValue(Resources.Error("Network connection error!"))
            } catch (e: HttpException) {
                _resultProfile.postValue(Resources.Error("Error HTTP: ${e.message}"))
            } catch (e: Exception) {
                _resultProfile.postValue(Resources.Error("An unknown error has occurred!"))
            }
        }
    }

    private val _searchBlogs: MutableLiveData<Resources<Blogs>?> = MutableLiveData()
    val searchBlogs: LiveData<Resources<Blogs>?> get() = _searchBlogs
    fun clearSearchBlogs() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _searchBlogs.value = null
            } catch (e: Exception) {
                _searchBlogs.postValue(Resources.Error("An unknown error has occurred!"))
            }
        }
    }

    fun searchBlogs(token: String, title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.searchBlogs(token, title)
                if (response.isSuccessful) {
                   // _searchBlogs.postValue(null)
                    val user = response.body()!!
                    _searchBlogs.postValue(Resources.Success(user))
                } else {
                    val errorMessage = "Search unsuccessful: ${response.message()}"
                    _searchBlogs.postValue(Resources.Error(errorMessage))
                }
            } catch (e: IOException) {
                _searchBlogs.postValue(Resources.Error("Network connection error!"))
            } catch (e: HttpException) {
                _searchBlogs.postValue(Resources.Error("Error HTTP: ${e.message}"))
            } catch (e: Exception) {
                _searchBlogs.postValue(Resources.Error("An unknown error has occurred!"))
            }
        }
    }

    fun user(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getUser(token)
                if (response.isSuccessful) {
                    val user = response.body()!!
                    _getUser.postValue(Resources.Success(user))
                } else {
                    val errorMessage = "User unsuccessful: ${response.message()}"
                    _getUser.postValue(Resources.Error(errorMessage))
                }
            } catch (e: IOException) {
                _getUser.postValue(Resources.Error("Network connection error!"))
            } catch (e: HttpException) {
                _getUser.postValue(Resources.Error("Error HTTP: ${e.message}"))
            } catch (e: Exception) {
                _getUser.postValue(Resources.Error("An unknown error has occurred!"))
            }

        }
    }

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

    fun slide() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getSlide()
                if (response.isSuccessful) {
                    val slide = response.body()!!
                    _getSlide.postValue(Resources.Success(slide))
                } else {
                    val errorMessage = "Slide unsuccessful: ${response.message()}"
                    _getSlide.postValue(Resources.Error(errorMessage))
                }
            } catch (e: IOException) {
                _getSlide.postValue(Resources.Error("Network connection error!"))
            } catch (e: HttpException) {
                _getSlide.postValue(Resources.Error("Error HTTP: ${e.message}"))
            } catch (e: Exception) {
                _getSlide.postValue(Resources.Error("An unknown error has occurred!"))
            }

        }
    }

}