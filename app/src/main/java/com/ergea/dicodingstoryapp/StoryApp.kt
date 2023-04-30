package com.ergea.dicodingstoryapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

@HiltAndroidApp
class StoryApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: StoryApp private set
    }
}