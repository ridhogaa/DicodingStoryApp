package com.ergea.dicodingstoryapp.data.remote.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ergea.dicodingstoryapp.data.remote.model.story.DetailStoryResponse
import com.ergea.dicodingstoryapp.data.remote.model.story.GetAllStoryResponse
import com.ergea.dicodingstoryapp.data.remote.service.StoryApiService
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import org.mockito.kotlin.mock

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

class StoryRemoteDataSourceImplTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var storyRemoteDataSource: StoryRemoteDataSource
    private lateinit var storyApiService: StoryApiService

    @Before
    fun setUp() {
        storyApiService = mock()
        storyRemoteDataSource =
            StoryRemoteDataSourceImpl(storyApiService)
    }

    @Test
    fun getAllStories() = runBlocking {
        val correct = mockk<GetAllStoryResponse>()
        Mockito.`when`(storyApiService.getAllStories("token", 1)).thenReturn(correct)
        val response = storyRemoteDataSource.getAllStories("token", 1)
        assertEquals(response, correct)
    }

    @Test
    fun getDetailStory() = runBlocking {
        val correct = mockk<DetailStoryResponse>()
        Mockito.`when`(storyApiService.getDetailStory("token", "1")).thenReturn(correct)
        val response = storyRemoteDataSource.getDetailStory("token", "1")
        assertEquals(response, correct)
    }
}