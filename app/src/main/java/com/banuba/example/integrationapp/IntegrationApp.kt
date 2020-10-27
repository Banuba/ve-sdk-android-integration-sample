package com.banuba.example.integrationapp

import android.app.Application
import com.banuba.example.integrationapp.videoeditor.di.VideoeditorKoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class IntegrationApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@IntegrationApp)

            // pass the customized Koin module that implements required dependencies.
            modules(VideoeditorKoinModule().module)
        }
    }
}