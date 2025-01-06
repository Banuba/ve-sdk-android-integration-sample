package com.banuba.example.integrationapp

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.util.Size
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.LifecycleCoroutineScope
import com.banuba.sdk.core.ext.obtainReadFileDescriptor
import kotlinx.coroutines.launch
import java.io.File

// Helper methods not required for the SDK integration
object Utils {
    fun previewExportedImage(
        activity: AppCompatActivity,
        uri: Uri
    ) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(uri, "image/*")

        try {
            activity.startActivity(intent)
        } catch (e: Exception) {
            Log.e(SampleApp.TAG, "Can't handle intent")
        }
    }
}