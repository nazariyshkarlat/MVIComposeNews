package com.example.myapplication

import android.app.Application
import com.example.myapplication.di.DI

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DI.initialize(this, DI.Config.RELEASE)
    }

}