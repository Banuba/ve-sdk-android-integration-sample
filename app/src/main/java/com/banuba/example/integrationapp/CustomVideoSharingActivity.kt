package com.banuba.example.integrationapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.banuba.sdk.core.ui.ext.addFragment
import com.banuba.sdk.export.data.ExportResult
import com.banuba.sdk.export.utils.EXTRA_EXPORTED_SUCCESS
import com.banuba.sdk.veui.ui.sharing.VideoSharingFragment

// Sample implementation of an Activity that shows SDK sharing screen.
// COPY THIS FILE IF YOU WANT TO USE SHARING FUNCTIONALITY THROUGH THE SDK
class CustomVideoSharingActivity : AppCompatActivity(R.layout.activity_video_sharing) {

    companion object {
        // Set up your Facebook app id
        const val FACEBOOK_APP_ID = ""
    }

    private val exportResult by lazy(LazyThreadSafetyMode.NONE) {
        intent?.getParcelableExtra<ExportResult.Success>(EXTRA_EXPORTED_SUCCESS)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val videoSharingFragment = VideoSharingFragment.newInstance(
            exportResult = exportResult,
            fbAppId = FACEBOOK_APP_ID
        )
        supportFragmentManager.addFragment(
            videoSharingFragment,
            VideoSharingFragment.TAG,
            R.id.fragmentContainer,
            false
        )
    }
}
