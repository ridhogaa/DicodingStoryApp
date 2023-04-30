package com.ergea.dicodingstoryapp.data.remote.datasource

import com.ergea.dicodingstoryapp.data.remote.model.auth.LoginBody
import com.ergea.dicodingstoryapp.data.remote.model.auth.LoginResponse
import com.ergea.dicodingstoryapp.data.remote.model.auth.RegisterBody
import com.ergea.dicodingstoryapp.data.remote.model.auth.RegisterResponse
import com.ergea.dicodingstoryapp.data.remote.service.AuthApiService
import javax.inject.Inject

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

interface AuthRemoteDataSource {
    suspend fun login(loginBody: LoginBody): LoginResponse
    suspend fun register(registerBody: RegisterBody): RegisterResponse
}

class AuthRemoteDataSourceImpl @Inject constructor(private val authApiService: AuthApiService) :
    AuthRemoteDataSource {
    override suspend fun login(loginBody: LoginBody): LoginResponse =
        authApiService.login(loginBody)

    override suspend fun register(registerBody: RegisterBody): RegisterResponse =
        authApiService.register(registerBody)

}