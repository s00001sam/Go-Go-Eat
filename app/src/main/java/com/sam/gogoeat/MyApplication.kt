package com.sam.gogoeat

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        MyApplication.appContext = applicationContext
    }
}