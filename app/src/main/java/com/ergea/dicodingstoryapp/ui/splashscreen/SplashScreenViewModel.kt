package com.ergea.dicodingstoryapp.ui.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ergea.dicodingstoryapp.data.repository.AuthRepository
import com.ergea.dicodingstoryapp.data.repository.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val settingRepository: SettingRepository
) : ViewModel() {
    fun getToken() = authRepository.getToken().asLiveData()
    fun getTheme() = settingRepository.getTheme().asLiveData()
}