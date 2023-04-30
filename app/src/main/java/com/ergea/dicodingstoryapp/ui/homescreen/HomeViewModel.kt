package com.ergea.dicodingstoryapp.ui.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ergea.dicodingstoryapp.data.remote.model.story.ListStoryItem
import com.ergea.dicodingstoryapp.data.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _story = MutableStateFlow<PagingData<ListStoryItem>>(PagingData.empty())
    val story = _story.asStateFlow()

    fun getAllStories() {
        storyRepository.getAllStories().cachedIn(viewModelScope).onEach {
            _story.value = it
        }.launchIn(viewModelScope)
    }

}