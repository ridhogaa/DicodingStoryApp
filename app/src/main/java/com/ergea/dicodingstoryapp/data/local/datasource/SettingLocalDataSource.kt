package com.ergea.dicodingstoryapp.data.local.datasource

import com.ergea.dicodingstoryapp.data.local.preferences.SettingDataStoreManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

interface SettingLocalDataSource {
    fun getTheme(): Flow<Boolean>
    suspend fun setTheme(condition: Boolean)
}

class SettingLocalDataSourceImpl @Inject constructor(private val settingDataStoreManager: SettingDataStoreManager) :
    SettingLocalDataSource {
    override fun getTheme(): Flow<Boolean> = settingDataStoreManager.getTheme

    override suspend fun setTheme(condition: Boolean) = settingDataStoreManager.setTheme(condition)

}
