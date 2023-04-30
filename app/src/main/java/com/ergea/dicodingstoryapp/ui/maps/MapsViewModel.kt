package com.ergea.dicodingstoryapp.ui.maps

import androidx.lifecycle.*
import com.ergea.dicodingstoryapp.data.remote.model.story.GetAllStoryResponse
import com.ergea.dicodingstoryapp.data.repository.AuthRepository
import com.ergea.dicodingstoryapp.data.repository.SettingRepository
import com.ergea.dicodingstoryapp.data.repository.StoryRepository
import com.ergea.dicodingstoryapp.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapsViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
    private val authRepository: AuthRepository,
    private val settingRepository: SettingRepository
) : ViewModel() {

    private val _stories = MutableLiveData<Resource<GetAllStoryResponse>>()
    val stories: LiveData<Resource<GetAllStoryResponse>> get() = _stories

    fun getAllStories(token: String, page: Int = 1, size: Int = 100, withLocation: Int = 0) {
        _stories.postValue(Resource.Loading())
        viewModelScope.launch {
            _stories.postValue(
                storyRepository.getAllStoriesWithMaps(
                    token,
                    page,
                    size,
                    withLocation
                )
            )
        }
    }

    fun getTheme(): LiveData<Boolean> = settingRepository.getTheme().asLiveData()

    fun getToken() = authRepository.getToken().asLiveData()

}