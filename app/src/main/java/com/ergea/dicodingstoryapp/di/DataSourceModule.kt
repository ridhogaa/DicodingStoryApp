package com.ergea.dicodingstoryapp.di

import com.ergea.dicodingstoryapp.data.local.datasource.AuthLocalDataSource
import com.ergea.dicodingstoryapp.data.local.datasource.AuthLocalDataSourceImpl
import com.ergea.dicodingstoryapp.data.local.datasource.SettingLocalDataSource
import com.ergea.dicodingstoryapp.data.local.datasource.SettingLocalDataSourceImpl
import com.ergea.dicodingstoryapp.data.remote.datasource.AuthRemoteDataSource
import com.ergea.dicodingstoryapp.data.remote.datasource.AuthRemoteDataSourceImpl
import com.ergea.dicodingstoryapp.data.remote.datasource.StoryRemoteDataSource
import com.ergea.dicodingstoryapp.data.remote.datasource.StoryRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun provideAuthLocalDataSource(authLocalDataSourceImpl: AuthLocalDataSourceImpl): AuthLocalDataSource

    @Binds
    abstract fun provideAuthRemoteDataSource(authRemoteDataSourceImpl: AuthRemoteDataSourceImpl): AuthRemoteDataSource

    @Binds
    abstract fun provideStoryRemoteDataSource(storyRemoteDataSourceImpl: StoryRemoteDataSourceImpl): StoryRemoteDataSource

    @Binds
    abstract fun provideSettingLocalDataSource(settingLocalDataSourceImpl: SettingLocalDataSourceImpl): SettingLocalDataSource
}