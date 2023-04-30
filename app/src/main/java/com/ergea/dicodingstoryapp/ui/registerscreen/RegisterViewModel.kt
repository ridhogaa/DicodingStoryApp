package com.ergea.dicodingstoryapp.ui.registerscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ergea.dicodingstoryapp.data.remote.model.auth.RegisterBody
import com.ergea.dicodingstoryapp.data.remote.model.auth.RegisterResponse
import com.ergea.dicodingstoryapp.data.repository.AuthRepository
import com.ergea.dicodingstoryapp.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _register = MutableLiveData<Resource<RegisterResponse>>()
    val register: LiveData<Resource<RegisterResponse>> get() = _register

    fun register(registerBody: RegisterBody) {
        _register.postValue(Resource.Loading())
        viewModelScope.launch {
            _register.postValue(authRepository.register(registerBody))
        }
    }

}