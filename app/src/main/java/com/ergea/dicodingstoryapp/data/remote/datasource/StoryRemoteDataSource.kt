package com.ergea.dicodingstoryapp.data.remote.datasource

import com.ergea.dicodingstoryapp.data.remote.model.story.AddStoryResponse
import com.ergea.dicodingstoryapp.data.remote.model.story.DetailStoryResponse
import com.ergea.dicodingstoryapp.data.remote.model.story.GetAllStoryResponse
import com.ergea.dicodingstoryapp.data.remote.service.StoryApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

interface StoryRemoteDataSource {
    suspend fun getAllStories(
        token: String,
        page: Int
    ): GetAllStoryResponse

    suspend fun getAllStoriesWithMaps(
        token: String,
        page: Int,
        size: Int,
        withLocation: Int
    ): GetAllStoryResponse

    suspend fun addNewStory(
        token: String,
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: Float? = null,
        lon: Float? = null
    ): AddStoryResponse

    suspend fun getDetailStory(token: String, id: String): DetailStoryResponse
}

class StoryRemoteDataSourceImpl @Inject constructor(private val storyApiService: StoryApiService) :
    StoryRemoteDataSource {
    override suspend fun getAllStories(
        token: String,
        page: Int
    ): GetAllStoryResponse = storyApiService.getAllStories(token, page)

    override suspend fun getAllStoriesWithMaps(
        token: String,
        page: Int,
        size: Int,
        withLocation: Int
    ): GetAllStoryResponse = storyApiService.getAllStories(token, page, size, withLocation)

    override suspend fun addNewStory(
        token: String,
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: Float?,
        lon: Float?
    ): AddStoryResponse = storyApiService.addNewStory(token, photo, description, lat, lon)

    override suspend fun getDetailStory(token: String, id: String): DetailStoryResponse =
        storyApiService.getDetailStory(token, id)

}