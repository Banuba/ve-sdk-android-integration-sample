package com.banuba.example.integrationapp

import android.app.Application
import com.banuba.example.integrationapp.videoeditor.di.VideoEditorKoinModule
import com.banuba.sdk.arcloud.di.ArCloudKoinModule
import com.banuba.sdk.audiobrowser.di.AudioBrowserKoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class IntegrationKotlinApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@IntegrationKotlinApp)

            // pass the customized Koin module that implements required dependencies.
            modules(
                ArCloudKoinModule().module,
                VideoEditorKoinModule().module,
                AudioBrowserKoinModule().module
            )
        }
    }
}