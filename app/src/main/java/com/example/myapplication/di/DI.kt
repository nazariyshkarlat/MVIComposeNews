package com.example.myapplication.di

import android.app.Application
import com.example.myapplication.di.features.createNewsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

object DI {

    sealed class Config {
        object RELEASE : Config()
        object TEST : Config()
    }

    fun initialize(application: Application, configuration: Config = DI.Config.RELEASE) {
        startKoin {
            androidLogger()
            androidContext(application)
            modules(
                networkModule,
                createNewsModule(configuration),
                )
        }
    }


}