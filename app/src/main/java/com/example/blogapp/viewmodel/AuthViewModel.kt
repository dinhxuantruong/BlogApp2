package com.example.blogapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blogapp.model.AcceptOTP
import com.example.blogapp.model.Auth
import com.example.blogapp.model.ForgetPass
import com.example.blogapp.model.Register
import com.example.blogapp.model.response.ResultMessage
import com.example.blogapp.model.response.User
import com.example.blogapp.model.sendOTP
import com.example.blogapp.repository.authRepository.AuthRepository
import com.example.blogapp.utils.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginResult: MutableLiveData<Resources<User>> = MutableLiveData()
    val loginResult: LiveData<Resources<User>> get() = _loginResult
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registerResult : MutableLiveData<Resources<ResultMessage>> = MutableLiveData()
    val registerResult : LiveData<Resources<ResultMessage>> = _registerResult

    private val _acceptResult: MutableLiveData<Resources<ResultMessage>> = MutableLiveData()
    val acceptResult: LiveData<Resources<ResultMessage>> = _acceptResult

    private val _resultOTP: MutableLiveData<Resources<ResultMessage>> = MutableLiveData()
    val resultOTP: LiveData<Resources<ResultMessage>> = _resultOTP

    private val _resultCheckAccount : MutableLiveData<Resources<ResultMessage>> = MutableLiveData()
    val resultCheckAccount : LiveData<Resources<ResultMessage>> = _resultCheckAccount

    private val _resultForgetPass: MutableLiveData<Resources<ResultMessage>> = MutableLiveData()
    val resultForget: LiveData<Resources<ResultMessage>> = _resultForgetPass

    fun authForgetPassword(forgetPass: ForgetPass) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.postValue(true) // Show ProgressBar
                val response = repository.authForgetPassword(forgetPass)
                if (response.isSuccessful) {
                    val resultMessage = response.body()!!
                    _resultForgetPass.postValue(Resources.Success(resultMessage))
                } else {
                    val errorMessage = "Password change failed: ${response.message()}"
                    _resultForgetPass.postValue(Resources.Error(errorMessage))
                }
            } catch (e: IOException) {
                _resultForgetPass.postValue(Resources.Error("Network connection error!"))
            } catch (e: HttpException) {
                _resultForgetPass.postValue(Resources.Error("Error HTTP: ${e.message}"))
            } catch (e: Exception) {
                _resultForgetPass.postValue(Resources.Error("An unknown error has occurred!"))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
    fun authCheckAccount(sendOTP: sendOTP){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.postValue(true) // Show ProgressBar
                val response = repository.authCheckAccount(sendOTP)
                if (response.isSuccessful) {
                    val resultMessage = response.body()!!
                    _resultCheckAccount.postValue(Resources.Success(resultMessage))
                } else {
                    val errorMessage = "Account does not exist : ${response.message()}"
                    _resultCheckAccount.postValue(Resources.Error(errorMessage))
                }
            } catch (e: IOException) {
                _resultCheckAccount.postValue(Resources.Error("Network connection error!"))
            } catch (e: HttpException) {
                _resultCheckAccount.postValue(Resources.Error("Error HTTP: ${e.message}"))
            } catch (e: Exception) {
                _resultCheckAccount.postValue(Resources.Error("An unknown error has occurred!"))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
    //OTP register
    fun authAcceptRegister(acceptOTP: AcceptOTP) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.postValue(true) // Show ProgressBar
                val response = repository.authAcceptRegister(acceptOTP)
                if (response.isSuccessful) {
                    val resultMessage = response.body()!!
                    _acceptResult.postValue(Resources.Success(resultMessage))
                } else {
                    val errorMessage = "Registration failed: ${response.message()}"
                    _acceptResult.postValue(Resources.Error(errorMessage))
                }
            } catch (e: IOException) {
                _acceptResult.postValue(Resources.Error("Network connection error!"))
            } catch (e: HttpException) {
                _acceptResult.postValue(Resources.Error("Error HTTP: ${e.message}"))
            } catch (e: Exception) {
                _acceptResult.postValue(Resources.Error("An unknown error has occurred!"))
            } finally {
                _isLoading.postValue(false) // Hide ProgressBar
            }
        }
    }


    fun authSendOTP(sendOTP: sendOTP) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //Log.e("email",email)
                _isLoading.postValue(true) // Show ProgressBar
                val response = repository.authSendOtp(sendOTP)
                if (response.isSuccessful) {
                    val resultMessage = response.body()!!
                    _resultOTP.postValue(Resources.Success(resultMessage))
                } else {
                    val errorMessage = "Registration failed OTP: ${response.message()}"
                    _resultOTP.postValue(Resources.Error(errorMessage))
                }
            } catch (e: IOException) {
                _resultOTP.postValue(Resources.Error("Network connection error!"))
            } catch (e: HttpException) {
                _resultOTP.postValue(Resources.Error("Error HTTP: ${e.message}"))
            } catch (e: Exception) {
                _resultOTP.postValue(Resources.Error("An unknown error has occurred!"))
            } finally {
                _isLoading.postValue(false) // Hide ProgressBar
            }
        }
    }

    fun authRegister(register: Register) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.postValue(true) // Show ProgressBar
                val response = repository.authRegister(register)
                if (response.isSuccessful) {
                    val resultMessage = response.body()!!
                    _registerResult.postValue(Resources.Success(resultMessage))
                } else {
                    val errorMessage = "Register unsuccessful: ${response.message()}"
                    _registerResult.postValue(Resources.Error(errorMessage))
                }
            } catch (e: IOException) {
                _registerResult.postValue(Resources.Error("Network connection error!"))
            } catch (e: HttpException) {
                _registerResult.postValue(Resources.Error("Error HTTP: ${e.message}"))
            } catch (e: Exception) {
                _registerResult.postValue(Resources.Error("An unknown error has occurred!"))
            } finally {
                _isLoading.postValue(false) // Hide ProgressBar
            }
        }
    }
    fun login(auth: Auth) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val response = repository.login(auth)
                if (response.isSuccessful) {
                    val user = response.body()!!
                    repository.saveAccessToken(user.user.access_token)
                    _loginResult.postValue(Resources.Success(user))
                } else {
                    val errorMessage = "Login unsuccessful: ${response.message()}"
                    _loginResult.postValue(Resources.Error(errorMessage))
                }
            } catch (e: IOException) {
                _loginResult.postValue(Resources.Error("Network connection error!"))
            } catch (e: HttpException) {
                _loginResult.postValue(Resources.Error("Error HTTP: ${e.message}"))
            } catch (e: Exception) {
                _loginResult.postValue(Resources.Error("An unknown error has occurred!"))
            }finally {
                _isLoading.postValue(false)
            }

        }
    }



}