package com.banuba.example.integrationapp

import android.app.Application
import android.util.Log
import com.banuba.sdk.core.EditorUtilityManager
import com.banuba.sdk.core.license.BanubaVideoEditor
import com.banuba.sdk.pe.BanubaPhotoEditor
import org.koin.android.ext.android.getKoin
import org.koin.core.context.stopKoin
import org.koin.core.error.InstanceCreationException

class SampleApp : Application() {

    companion object {
        const val TAG = "BanubaSdk"

        // Please set your license token for Banuba Video Editor SDK or Photo Editor SDK
        private const val LICENSE_TOKEN = SET LICENSE TOKEN

        const val ERR_SDK_NOT_INITIALIZED =
            "Banuba Video Editor SDK or Photo Editor SDK is not initialized: license token is unknown or incorrect.\nPlease check your license token or contact Banuba"
        const val ERR_LICENSE_REVOKED =
            "License is revoked or expired. Please contact Banuba https://www.banuba.com/faq/kb-tickets/new"
    }
    // Manages Photo Editor SDK
    var photoEditor: BanubaPhotoEditor? = null

    override fun onCreate() {
        super.onCreate()
        preparePhotoEditor()
    }

    fun preparePhotoEditor() {
        Log.d(TAG, "Prepare Photo Editor SDK")
        // Photo Editor SDK auto releases its resources
        photoEditor = BanubaPhotoEditor.initialize(LICENSE_TOKEN)
    }
}
