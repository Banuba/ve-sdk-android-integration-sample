package com.banuba.example.integrationapp

import android.app.Application
import android.util.Log
import com.banuba.sdk.core.EditorUtilityManager
import com.banuba.sdk.core.license.EditorSdk
import org.koin.android.ext.android.getKoin
import org.koin.core.context.stopKoin
import org.koin.core.error.InstanceCreationException

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

    var editorSdk: EditorSdk? = null

    override fun onCreate() {
        super.onCreate()
        initSdk()
    }

    fun initSdk() {
        // Video and Photo SDK share license management system.
        if (editorSdk == null) {
            Log.d(TAG, "Prepare Video Editor SDK")

            // Initialize Video Editor
            VideoEditorModule().initialize(this@SampleApp)

            // Initialize Banuba Video Editor SDK
            if (editorSdk == null) {
                editorSdk = EditorSdk.initialize(LICENSE_TOKEN)
            }

            if (editorSdk == null) {
                // Token you provided is not correct - empty or truncated
                Log.e(TAG, ERR_SDK_NOT_INITIALIZED)
            }
        }
    }

    // Call it to release VE SDK before opening PE
    private fun releaseVideoEditor() {
        Log.d(TAG, "Release Video Editor SDK")
        releaseUtilityManager()
        stopKoin()
        editorSdk = null
    }

    private fun releaseUtilityManager() {
        val utilityManager = try {
            // EditorUtilityManager is NULL when the token is expired or revoked.
            // This dependency is not explicitly created in DI.
            getKoin().getOrNull<EditorUtilityManager>()
        } catch (e: InstanceCreationException) {
            Log.w(TAG, "EditorUtilityManager was not initialized!", e)
            null
        }

        utilityManager?.release()
    }
}
