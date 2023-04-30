package com.ergea.dicodingstoryapp.ui.loginscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ergea.dicodingstoryapp.data.remote.model.auth.LoginBody
import com.ergea.dicodingstoryapp.data.remote.model.auth.LoginResponse
import com.ergea.dicodingstoryapp.data.repository.AuthRepository
import com.ergea.dicodingstoryapp.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _login = MutableLiveData<Resource<LoginResponse>>()
    val login: LiveData<Resource<LoginResponse>> get() = _login

    fun login(loginBody: LoginBody) {
        _login.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            val response = authRepository.login(loginBody)
            viewModelScope.launch(Dispatchers.Main) {
                _login.postValue(response)
            }
        }
    }
}