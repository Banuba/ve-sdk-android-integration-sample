package com.banuba.example.integrationapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.banuba.sdk.cameraui.data.PipConfig
import com.banuba.sdk.core.ui.ext.visible
import com.banuba.sdk.export.data.ExportResult
import com.banuba.sdk.export.utils.EXTRA_EXPORTED_SUCCESS
import com.banuba.sdk.ve.flow.VideoCreationActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // Handle Video Editor export results
    private val videoEditorExportResult =
        registerForActivityResult(CustomExportResultVideoContract()) { exportResult ->
            AlertDialog.Builder(this)
                .setMessage("Export result")
                .setPositiveButton("Preview Video") { _,_ -> openExportResult(exportResult) }
                .setNegativeButton("Open Sharing") { _,_ -> openSharingActivity(exportResult)}
                .setNeutralButton("Close") { _,_ -> }
                .create()
                .show()
        }

    private fun openExportResult(exportResult: ExportResult?) {
        if (exportResult is ExportResult.Success) {
            exportResult.videoList.firstOrNull()?.let {
                Utils.playExportedVideo(this@MainActivity, it.sourceUri)
            }
        }
    }

    private fun openSharingActivity(result: ExportResult?) {
        val intent =
            Intent(getString(com.banuba.sdk.export.R.string.export_action_name, packageName)).apply {
                if (result is ExportResult.Success) {
                    putExtra(EXTRA_EXPORTED_SUCCESS, result)
                }
                val flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                addFlags(flags)
            }
        startActivity(intent)
    }

    // Opens system app to pick video for PIP
    private val requestVideoOpenPIP = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { videoPipUri ->
        if (videoPipUri != Uri.EMPTY && videoPipUri != null) {
            openVideoEditor(pipVideo = videoPipUri)
        }
    }

    // Opens system app to pick image for Trimmer
    private val requestImageOpenTrimmer = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { imageUri ->
        if (imageUri != Uri.EMPTY && imageUri != null) {
            val slideShowList =
                Utils.createSlideShowList(
                    applicationContext,
                    lifecycleScope,
                    imageUri
                )
            openVideoEditorTrimmerWithSlideShow(slideShowList)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Handle Video Editor license flow
        val videoEditor = (application as SampleApp).videoEditor
        if (videoEditor == null) {
            licenseStateView.visible()
            licenseStateView.text = SampleApp.ERR_SDK_NOT_INITIALIZED
            return
        }

        // Might take up to 1 sec in the worst case.
        // Please optimize use of this function in your project to bring the best user experience
        videoEditor.getLicenseState { isValid ->
            if (isValid) {
                // ✅ License is active, all good
                // You can show button that opens Video Editor or
                // Start Video Editor right away
                btnVideoEditor.setOnClickListener {
                    openVideoEditor()
                }
                btnPiPVideoEditor.setOnClickListener {
                    requestVideoOpenPIP.launch("video/*")
                }
                btnDraftsVideoEditor.setOnClickListener {
                    openVideoEditorDrafts()
                }
                btnSlideShowVideoEditorTrimmer.setOnClickListener {
                    requestImageOpenTrimmer.launch("image/*")
                }
            } else {
                // ❌ Use of Video Editor is restricted. License is revoked or expired.
                licenseStateView.text = SampleApp.ERR_LICENSE_REVOKED
                Log.w(SampleApp.TAG, SampleApp.ERR_LICENSE_REVOKED)

                licenseStateView.visible()
                btnVideoEditor.isEnabled = false
                btnPiPVideoEditor.isEnabled = false
                btnDraftsVideoEditor.isEnabled = false
                btnSlideShowVideoEditorTrimmer.isEnabled = false
            }
        }
    }

    private fun openVideoEditor(pipVideo: Uri = Uri.EMPTY) {
        val videoCreationIntent = VideoCreationActivity.startFromCamera(
            context = this,
            // set PiP video configuration
            pictureInPictureConfig = PipConfig(
                video = pipVideo,
                openPipSettings = false
            ),
            // setup what kind of action you want to do with VideoCreationActivity
            // setup data that will be acceptable during export flow
            additionalExportData = null,
            // set TrackData object if you open VideoCreationActivity with preselected music track
            audioTrackData = null
        )
        videoEditorExportResult.launch(videoCreationIntent)
    }

    private fun openVideoEditorDrafts() {
        videoEditorExportResult.launch(VideoCreationActivity.startFromDrafts(this))
    }

    private fun openVideoEditorTrimmerWithSlideShow(videos: List<Uri>) {
        videoEditorExportResult.launch(
            VideoCreationActivity.startFromTrimmer(
                this,
                videos.toTypedArray()
            )
        )
    }
}
