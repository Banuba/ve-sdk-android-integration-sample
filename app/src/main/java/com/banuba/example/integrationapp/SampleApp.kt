package com.banuba.example.integrationapp

import android.app.Application
import android.util.Log
import com.banuba.sdk.audiobrowser.data.AudioBrowserMusicProvider
import com.banuba.sdk.audiobrowser.di.AudioBrowserKoinModule
import com.banuba.sdk.effectplayer.adapter.BanubaEffectPlayerKoinModule
import com.banuba.sdk.gallery.di.GalleryKoinModule
import com.banuba.sdk.ve.flow.VideoEditorSDK

class SampleApp : Application() {

    companion object {
        const val TAG = "BanubaSdk"

        // Please set your license token for Banuba Video Editor SDK or Photo Editor SDK
        private const val LICENSE_TOKEN =

        const val ERR_SDK_NOT_INITIALIZED =
            "Banuba Video Editor SDK or Photo Editor SDK is not initialized: license token is unknown or incorrect.\nPlease check your license token or contact Banuba"
        const val ERR_LICENSE_REVOKED =
            "License is revoked or expired. Please contact Banuba https://www.banuba.com/faq/kb-tickets/new"
    }

    var editorSdk: VideoEditorSDK? = null

    override fun onCreate() {
        super.onCreate()
        initSdk()
    }

    fun initSdk() {
        // Video and Photo SDK share license management system.
        if (editorSdk == null) {
            Log.d(TAG, "Prepare Video Editor SDK")

            // Initialize Banuba Video Editor SDK

            editorSdk = VideoEditorSDK.Builder(applicationContext).apply {
                // Optional AudioBrowser module for music providers: BanubaMusic, Soundstripe, Local
                addModule(AudioBrowserKoinModule().module)
                // Required if AudioBrowser is added.
                setMusicTrackProvider(AudioBrowserMusicProvider())

                // Optional Banuba Gallery module for selecting videos and photos from device
                addModule(GalleryKoinModule().module)

                // Optional module to enable Face AR product
                addModule(BanubaEffectPlayerKoinModule().module)
            }.initialize(LICENSE_TOKEN)
        }

        if (editorSdk == null) {
            // Token you provided is not correct - empty or truncated
            Log.e(TAG, ERR_SDK_NOT_INITIALIZED)
        }
    }

    // Call it to release VE SDK before opening PE
    private fun releaseVideoEditor() {
        Log.d(TAG, "Release Video Editor SDK")
        editorSdk?.release()
        editorSdk = null
    }
}
