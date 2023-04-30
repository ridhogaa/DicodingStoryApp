package com.ergea.dicodingstoryapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ergea.dicodingstoryapp.data.remote.datasource.StoryRemoteDataSource
import com.ergea.dicodingstoryapp.data.remote.model.story.AddStoryResponse
import com.ergea.dicodingstoryapp.data.remote.model.story.DetailStoryResponse
import com.ergea.dicodingstoryapp.data.remote.model.story.GetAllStoryResponse
import com.ergea.dicodingstoryapp.data.remote.model.story.ListStoryItem
import com.ergea.dicodingstoryapp.data.remote.paging.StoryPagingSource
import com.ergea.dicodingstoryapp.wrapper.Resource
import com.ergea.dicodingstoryapp.wrapper.proceed
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

interface StoryRepository {
    fun getAllStories(): Flow<PagingData<ListStoryItem>>

    suspend fun getAllStoriesWithMaps(
        token: String,
        page: Int,
        size: Int,
        withLocation: Int
    ): Resource<GetAllStoryResponse>

    suspend fun addNewStory(
        token: String,
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: Float? = null,
        lon: Float? = null
    ): Resource<AddStoryResponse>

    suspend fun getDetailStory(token: String, id: String): Resource<DetailStoryResponse>
}

class StoryRepositoryImpl @Inject constructor(
    private val storyRemoteDataSource: StoryRemoteDataSource,
    private val storyPagingSource: StoryPagingSource
) : StoryRepository {
    override fun getAllStories(): Flow<PagingData<ListStoryItem>> =
        Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { storyPagingSource }
        ).flow


    override suspend fun getAllStoriesWithMaps(
        token: String,
        page: Int,
        size: Int,
        withLocation: Int
    ): Resource<GetAllStoryResponse> =
        proceed { storyRemoteDataSource.getAllStoriesWithMaps(token, page, size, withLocation) }


    override suspend fun addNewStory(
        token: String,
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: Float?,
        lon: Float?
    ): Resource<AddStoryResponse> =
        proceed { storyRemoteDataSource.addNewStory(token, photo, description, lat, lon) }

    override suspend fun getDetailStory(token: String, id: String): Resource<DetailStoryResponse> =
        proceed {
            storyRemoteDataSource.getDetailStory(token, id)
        }

}