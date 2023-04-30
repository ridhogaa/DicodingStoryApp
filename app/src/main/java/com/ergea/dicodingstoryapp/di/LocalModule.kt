package com.ergea.dicodingstoryapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ergea.dicodingstoryapp.data.local.preferences.AuthDataStoreManager
import com.ergea.dicodingstoryapp.data.local.preferences.SettingDataStoreManager
import com.ergea.dicodingstoryapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

private val Context.dataStorePreferences by preferencesDataStore(
    name = Constants.DATASTORE_PREFERENCES
)

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStorePreferences

    @Singleton
    @Provides
    fun provideAuthDataStoreManager(dataStore: DataStore<Preferences>): AuthDataStoreManager =
        AuthDataStoreManager(dataStore)

    @Singleton
    @Provides
    fun provideSettingDataStoreManager(dataStore: DataStore<Preferences>): SettingDataStoreManager =
        SettingDataStoreManager(dataStore)

}