package com.ergea.dicodingstoryapp.data.repository

import com.ergea.dicodingstoryapp.data.local.datasource.SettingLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

interface SettingRepository {
    fun getTheme(): Flow<Boolean>
    suspend fun setTheme(condition: Boolean)
}

class SettingRepositoryImpl @Inject constructor(private val settingLocalDataSource: SettingLocalDataSource) : SettingRepository {
    override fun getTheme(): Flow<Boolean> = settingLocalDataSource.getTheme()

    override suspend fun setTheme(condition: Boolean) = settingLocalDataSource.setTheme(condition)

}

