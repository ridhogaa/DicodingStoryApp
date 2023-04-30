package com.ergea.dicodingstoryapp.di

import com.ergea.dicodingstoryapp.data.local.datasource.AuthLocalDataSource
import com.ergea.dicodingstoryapp.data.remote.datasource.StoryRemoteDataSource
import com.ergea.dicodingstoryapp.data.remote.paging.StoryPagingSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

@Module
@InstallIn(SingletonComponent::class)
object PagingSourceModule {

    @Provides
    @Singleton
    fun provideStoryPagingSource(
        storyRemoteDataSource: StoryRemoteDataSource,
        authLocalDataSource: AuthLocalDataSource
    ): StoryPagingSource = StoryPagingSource(storyRemoteDataSource, authLocalDataSource)

}