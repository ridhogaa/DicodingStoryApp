package com.ergea.dicodingstoryapp.data.local.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ergea.dicodingstoryapp.data.local.preferences.SettingDataStoreManager
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
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

class SettingLocalDataSourceImplTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var settingLocalDataSource: SettingLocalDataSource
    private lateinit var settingDataStoreManager: SettingDataStoreManager

    @Before
    fun setUp() {
        settingDataStoreManager = mock()
        settingLocalDataSource =
            SettingLocalDataSourceImpl(settingDataStoreManager)
    }

    @Test
    fun getTheme() = runBlocking {
        val correct = mockk<Flow<Boolean>>()
        Mockito.`when`(settingDataStoreManager.getTheme).thenReturn(correct)
        val response = settingLocalDataSource.getTheme()
        assertEquals(response, correct)
    }

    @Test
    fun setTheme() = runBlocking {
        val correct = mockk<Unit>()
        Mockito.`when`(settingDataStoreManager.setTheme(any())).thenReturn(correct)
        val response = settingLocalDataSource.setTheme(any())
        assertEquals(response, correct)
    }
}