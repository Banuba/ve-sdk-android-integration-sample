package com.banuba.example.integrationapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.banuba.sdk.core.ui.ext.addFragment
import com.banuba.sdk.export.data.ExportResult
import com.banuba.sdk.export.utils.EXTRA_EXPORTED_SUCCESS
import com.banuba.sdk.veui.ui.sharing.VideoSharingFragment

class VideoSharingActivity : AppCompatActivity(R.layout.activity_video_sharing) {

    companion object {
        const val FB_APP_ID = "YOUR_FB_ID"
    }

    private val exportResult by lazy(LazyThreadSafetyMode.NONE) {
        intent?.getParcelableExtra<ExportResult.Success>(EXTRA_EXPORTED_SUCCESS)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val videoSharingFragment = VideoSharingFragment.newInstance(
            exportResult = exportResult,
            fbAppId = FB_APP_ID
        )
        supportFragmentManager.addFragment(
            videoSharingFragment,
            VideoSharingFragment.TAG,
            R.id.fragmentContainer,
            false
        )
    }
}
