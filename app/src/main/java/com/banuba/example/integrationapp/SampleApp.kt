package com.banuba.example.integrationapp

import android.app.Application
import android.util.Log
import com.banuba.sdk.token.storage.license.BanubaVideoEditor

class SampleApp : Application() {

    companion object {
        const val TAG = "BanubaVideoEditor"

        // Please set your license token for Banuba Video Editor SDK
        const val LICENSE_TOKEN = SET LICENSE TOKEN

        const val ERR_SDK_NOT_INITIALIZED = "Banuba Video Editor SDK is not initialized: license token is unknown or incorrect.\nPlease check your license token or contact Banuba"
        const val ERR_LICENSE_REVOKED = "License is revoked or expired. Please contact Banuba https://www.banuba.com/faq/kb-tickets/new"
    }

    var videoEditor: BanubaVideoEditor? = null

    override fun onCreate() {
        super.onCreate()

        // Initialize Banuba Video Editor SDK
        videoEditor = BanubaVideoEditor.initialize(LICENSE_TOKEN)

        if (videoEditor == null) {
            // Token you provided is not correct - empty or truncated
            Log.e(TAG, ERR_SDK_NOT_INITIALIZED)
        } else {
            VideoEditorModule().initialize(this@SampleApp)
        }
    }
}
