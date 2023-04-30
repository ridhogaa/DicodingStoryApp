package com.ergea.dicodingstoryapp.ui.detailstoryscreen

import androidx.lifecycle.*
import com.ergea.dicodingstoryapp.data.remote.model.story.DetailStoryResponse
import com.ergea.dicodingstoryapp.data.repository.AuthRepository
import com.ergea.dicodingstoryapp.data.repository.StoryRepository
import com.ergea.dicodingstoryapp.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailStoryViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _detailStory = MutableLiveData<Resource<DetailStoryResponse>>()
    val detailStory: LiveData<Resource<DetailStoryResponse>> get() = _detailStory

    fun getDetailStory(token: String, id: String) {
        _detailStory.postValue(Resource.Loading())
        viewModelScope.launch {
            _detailStory.postValue(storyRepository.getDetailStory(token, id))
        }
    }

    fun getToken() = authRepository.getToken().asLiveData()

}