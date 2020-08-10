package com.client.headlineshay

import android.app.Application
import com.facebook.stetho.Stetho
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApplication: Application(){


    override fun onCreate() {
        super.onCreate()
        //remove in production
        Stetho.initializeWithDefaults(this);

    }
}