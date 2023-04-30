package com.ergea.dicodingstoryapp.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ergea.dicodingstoryapp.data.local.datasource.AuthLocalDataSource
import com.ergea.dicodingstoryapp.data.remote.datasource.StoryRemoteDataSource
import com.ergea.dicodingstoryapp.data.remote.model.story.ListStoryItem
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

class StoryPagingSource @Inject constructor(
    private val storyRemoteDataSource: StoryRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource
) : PagingSource<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> =
        try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val token = authLocalDataSource.getToken().first()
            val responseData = storyRemoteDataSource.getAllStories(token, page).listStory!!

            LoadResult.Page(
                data = responseData,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
}