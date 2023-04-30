package com.ergea.dicodingstoryapp.data.local.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ergea.dicodingstoryapp.data.local.preferences.AuthDataStoreManager
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

class AuthLocalDataSourceImplTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var authLocalDataSource: AuthLocalDataSource
    private lateinit var authDataSourceManager: AuthDataStoreManager

    @Before
    fun setUp() {
        authDataSourceManager = mock()
        authLocalDataSource =
            AuthLocalDataSourceImpl(authDataSourceManager)
    }

    @Test
    fun getToken() = runBlocking {
        val correct = mockk<Flow<String>>()
        Mockito.`when`(authDataSourceManager.getToken).thenReturn(correct)
        val response = authLocalDataSource.getToken()
        assertEquals(response, correct)
    }

    @Test
    fun setToken() = runBlocking {
        val correct = mockk<Unit>()
        Mockito.`when`(authDataSourceManager.setToken(any())).thenReturn(correct)
        val response = authLocalDataSource.setToken(any())
        assertEquals(response, correct)
    }

    @Test
    fun clearToken() = runBlocking {
        val correct = mockk<Unit>()
        Mockito.`when`(authDataSourceManager.clearToken()).thenReturn(correct)
        val response = authLocalDataSource.clearToken()
        assertEquals(response, correct)
    }
}