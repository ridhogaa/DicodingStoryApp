package com.ergea.dicodingstoryapp.data.remote.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ergea.dicodingstoryapp.data.remote.model.auth.LoginBody
import com.ergea.dicodingstoryapp.data.remote.model.auth.LoginResponse
import com.ergea.dicodingstoryapp.data.remote.model.auth.RegisterBody
import com.ergea.dicodingstoryapp.data.remote.model.auth.RegisterResponse
import com.ergea.dicodingstoryapp.data.remote.service.AuthApiService
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import org.mockito.kotlin.mock

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

class AuthRemoteDataSourceImplTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var authRemoteDataSource: AuthRemoteDataSource
    private lateinit var authApiService: AuthApiService

    @Before
    fun setUp() {
        authApiService = mock()
        authRemoteDataSource =
            AuthRemoteDataSourceImpl(authApiService)
    }

    @Test
    fun login() = runBlocking {
        val correct = mockk<LoginResponse>()
        Mockito.`when`(authApiService.login(LoginBody("test", "test"))).thenReturn(correct)
        val response = authRemoteDataSource.login(LoginBody("test", "test"))
        assertEquals(response, correct)
    }

    @Test
    fun register() = runBlocking {
        val correct = mockk<RegisterResponse>()
        Mockito.`when`(authApiService.register(RegisterBody("test", "test", "test")))
            .thenReturn(correct)
        val response = authRemoteDataSource.register(RegisterBody("test", "test", "test"))
        assertEquals(response, correct)
    }
}