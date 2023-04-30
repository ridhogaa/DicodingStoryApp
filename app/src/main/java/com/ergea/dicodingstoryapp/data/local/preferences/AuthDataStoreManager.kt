package com.ergea.dicodingstoryapp.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ergea.dicodingstoryapp.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

class AuthDataStoreManager @Inject constructor(private val dataStore: DataStore<Preferences>) {

    val getToken: Flow<String> = dataStore.data.map {
        it[stringPreferencesKey(Constants.TOKEN_USER_KEY)] ?: ""
    }

    suspend fun setToken(token: String) {
        dataStore.edit {
            it[stringPreferencesKey(Constants.TOKEN_USER_KEY)] = "Bearer $token"
        }
    }

    suspend fun clearToken() {
        dataStore.edit {
            it.remove(stringPreferencesKey(Constants.TOKEN_USER_KEY))
        }
    }
}