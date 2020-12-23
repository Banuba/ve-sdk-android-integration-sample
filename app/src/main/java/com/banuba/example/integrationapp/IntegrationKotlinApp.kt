package com.banuba.example.integrationapp

import android.app.Application
import com.banuba.example.integrationapp.videoeditor.di.VideoEditorKoinModule
import com.banuba.sdk.arcloud.di.ArCloudKoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class IntegrationKotlinApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@IntegrationKotlinApp)

            // pass the customized Koin module that implements required dependencies.
            modules(
                    VideoEditorKoinModule().module,
                    ArCloudKoinModule().module
            )
        }
    }
}