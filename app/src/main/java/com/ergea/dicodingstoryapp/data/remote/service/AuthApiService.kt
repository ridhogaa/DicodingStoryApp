package com.ergea.dicodingstoryapp.data.remote.service

import com.ergea.dicodingstoryapp.data.remote.model.auth.LoginBody
import com.ergea.dicodingstoryapp.data.remote.model.auth.LoginResponse
import com.ergea.dicodingstoryapp.data.remote.model.auth.RegisterBody
import com.ergea.dicodingstoryapp.data.remote.model.auth.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

interface AuthApiService {

    @POST(LOGIN)
    suspend fun login(@Body loginBody: LoginBody): LoginResponse

    @POST(REGISTER)
    suspend fun register(@Body registerBody: RegisterBody): RegisterResponse

    companion object {
        const val LOGIN = "login"
        const val REGISTER = "register"
    }

}