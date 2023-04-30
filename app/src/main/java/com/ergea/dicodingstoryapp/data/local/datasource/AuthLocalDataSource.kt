package com.ergea.dicodingstoryapp.data.local.datasource

import com.ergea.dicodingstoryapp.data.local.preferences.AuthDataStoreManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

interface AuthLocalDataSource {
    fun getToken(): Flow<String>
    suspend fun setToken(token: String)
    suspend fun clearToken()
}

class AuthLocalDataSourceImpl @Inject constructor(private val authDataStoreManager: AuthDataStoreManager) :
    AuthLocalDataSource {
    override fun getToken(): Flow<String> = authDataStoreManager.getToken

    override suspend fun setToken(token: String) = authDataStoreManager.setToken(token)

    override suspend fun clearToken() = authDataStoreManager.clearToken()
}