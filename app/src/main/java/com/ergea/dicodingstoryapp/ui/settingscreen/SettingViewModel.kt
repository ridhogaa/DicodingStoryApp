package com.ergea.dicodingstoryapp.ui.settingscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ergea.dicodingstoryapp.data.repository.AuthRepository
import com.ergea.dicodingstoryapp.data.repository.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val settingRepository: SettingRepository
) : ViewModel() {

    fun getTheme(): LiveData<Boolean> = settingRepository.getTheme().asLiveData()

    fun setTheme(condition: Boolean) = viewModelScope.launch {
        settingRepository.setTheme(condition)
    }

    fun clearToken() = viewModelScope.launch(Dispatchers.IO) { authRepository.clear() }

}