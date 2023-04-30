package com.ergea.dicodingstoryapp.ui.addstoryscreen

import androidx.lifecycle.*
import com.ergea.dicodingstoryapp.data.remote.model.story.AddStoryResponse
import com.ergea.dicodingstoryapp.data.repository.AuthRepository
import com.ergea.dicodingstoryapp.data.repository.StoryRepository
import com.ergea.dicodingstoryapp.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _addStory = MutableLiveData<Resource<AddStoryResponse>>()
    val addStory: LiveData<Resource<AddStoryResponse>> get() = _addStory

    fun addNewStory(
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: Float? = null,
        lon: Float? = null
    ) = viewModelScope.launch(Dispatchers.IO) {
        _addStory.postValue(Resource.Loading())
        delay(1000L)
        _addStory.postValue(storyRepository.addNewStory(authRepository.getToken().first(), photo, description, lat, lon))
    }

}