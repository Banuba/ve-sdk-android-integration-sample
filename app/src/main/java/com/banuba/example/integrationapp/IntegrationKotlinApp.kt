package com.banuba.example.integrationapp

import android.app.Application
import com.banuba.example.integrationapp.videoeditor.di.VideoEditorKoinModule
import com.banuba.sdk.arcloud.di.ArCloudKoinModule
import com.banuba.sdk.audiobrowser.di.AudioBrowserKoinModule
import com.banuba.sdk.effectplayer.adapter.BanubaEffectPlayerKoinModule
import com.banuba.sdk.gallery.di.GalleryKoinModule
import com.banuba.sdk.token.storage.di.TokenStorageKoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class IntegrationKotlinApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@IntegrationKotlinApp)

            // pass the customized Koin module that implements required dependencies.
            modules(
                AudioBrowserKoinModule().module, // use this module only if you bought it
                ArCloudKoinModule().module,
                TokenStorageKoinModule().module,
                VideoEditorKoinModule().module,
                GalleryKoinModule().module,
                BanubaEffectPlayerKoinModule().module
            )
        }
    }
}
