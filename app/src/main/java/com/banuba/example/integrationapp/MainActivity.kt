package com.banuba.example.integrationapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.banuba.example.integrationapp.SampleApp.Companion.ERR_LICENSE_REVOKED
import com.banuba.example.integrationapp.SampleApp.Companion.ERR_SDK_NOT_INITIALIZED
import com.banuba.example.integrationapp.databinding.ActivityMainBinding
import com.banuba.sdk.cameraui.data.PipConfig
import com.banuba.sdk.core.ui.ext.visible
import com.banuba.sdk.export.data.ExportResult
import com.banuba.sdk.pe.PhotoCreationActivity
import com.banuba.sdk.pe.PhotoExportResultContract
import com.banuba.sdk.ve.flow.SdkIntegrationUtils
import com.banuba.sdk.ve.flow.VideoCreationActivity
import com.banuba.sdk.ve.flow.VideoExportResultContract


class MainActivity : AppCompatActivity() {

    // Handle Video Editor export results using VideoExportResultContract
    // The activity result contract receives the export result (e.g. success with exported video list, cancel, or error).
    private val videoEditorExportResult =
        registerForActivityResult(VideoExportResultContract()) { result ->
            // The dialog is used only to demonstrate and play an exported video file.

            // Release Video Editor SDK after exporting video and closing the latest SDK screen
            //(application as? SampleApp)?.releaseVideoEditor()
            Log.d(SampleApp.TAG, "Video Editor export result = $result")

            if (result is ExportResult.Success) {
                AlertDialog.Builder(this).setMessage("Export result")
                    .setPositiveButton("Play Video") { _, _ ->
                        result.videoList.firstOrNull()?.let {
                            SdkIntegrationUtils.playExportedVideo(this@MainActivity, it.sourceUri)
                        }
                    }
                    .setNeutralButton("Close") { _, _ ->
                    }.create().show()
            }
        }

    // Opens system app to pick video for PIP
    private val requestOpenVideoEditorPIP = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { videoPipUri ->
        if (videoPipUri != Uri.EMPTY && videoPipUri != null) {
            openVideoEditor(pipVideo = videoPipUri)
        }
    }

    // Opens system app to pick image for Trimmer
    private val requestOpenVideoEditorTrimmerWithImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { imageUri ->
        if (imageUri != Uri.EMPTY && imageUri != null) {
            val slideShowList =
                SdkIntegrationUtils.createSlideShowList(
                    applicationContext,
                    lifecycleScope,
                    imageUri
                )
            openVideoEditorTrimmerWithSlideShow(slideShowList)
        }
    }

    private val requestOpenPhotoEditorWithImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { imageUri ->
        if (imageUri == null || imageUri == Uri.EMPTY) {
            Log.w(SampleApp.TAG, "Please pick image to open Photo Editor SDK!")
        } else {
            startPhotoEditor(
                PhotoCreationActivity.startFromEditor(
                    applicationContext,
                    imageUri = imageUri
                )
            )
        }
    }

    private val photoEditorExportResult =
        registerForActivityResult(PhotoExportResultContract()) { uri ->
            if (uri == null || uri == Uri.EMPTY) {
                val errMessage =
                    "No exported image or the token does not support Photo Editor SDK"
                Log.w(SampleApp.TAG, errMessage)
                Toast.makeText(applicationContext, errMessage, Toast.LENGTH_SHORT).show()
                return@registerForActivityResult
            }

            Log.d(SampleApp.TAG, "Exported image uri = $uri")

            SdkIntegrationUtils.previewExportedImage(this, uri)
        }

    private var _binding: ActivityMainBinding? = null

    private val binding: ActivityMainBinding
        get() = requireNotNull(_binding)

    private val sampleApp: SampleApp
        get() = application as SampleApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        setContentView(binding.root)

        // Step 4: Initialize the SDK and Handle Video Editor license flow
        // The SDK checks the license activation state asynchronously.
        // A valid token is required to initialize and launch the Video Editor & Photo Editor.
        val videoEditor = sampleApp.editorSdk
        if (videoEditor == null) {
            binding.licenseStateView.visible()
            binding.licenseStateView.text = ERR_SDK_NOT_INITIALIZED
            return
        }

        // Check license validity before opening editor screens.
        // Might take up to 1 sec in the worst case.
        // Please optimize use of this function in your project to bring the best user experience
        videoEditor.getLicenseState { isValid ->
            if (isValid) {
                with(binding) {
                    // ✅ License is active, all good
                    // Step 5: Launch the Video Editor or other features from your Activity
                    // You can show buttons that open Video Editor, PiP, Drafts, Trimmer, Templates, or Photo Editor.
                    btnVideoEditor.setOnClickListener {
                        openVideoEditor()
                    }
                    btnPiPVideoEditor.setOnClickListener {
                        requestOpenVideoEditorPIP.launch("video/*")
                    }
                    btnDraftsVideoEditor.setOnClickListener {
                        openVideoEditorDrafts()
                    }
                    btnSlideShowVideoEditorTrimmer.setOnClickListener {
                        requestOpenVideoEditorTrimmerWithImage.launch("image/*")
                    }
                    btnOpenPhotoEditor.setOnClickListener {
                        // Start Photo Editor SDK from Gallery
                        startPhotoEditor(PhotoCreationActivity.startFromGallery(this@MainActivity))
                    }
                    btnOpenPhotoEditorImage.setOnClickListener {
                        // Start Photo Editor SDK with a specific picked image
                        requestOpenPhotoEditorWithImage.launch("image/*")
                    }
                    btnTemplatesVideoEditor.setOnClickListener {
                        openVideoEditorFromTemplates()
                    }
                }
            } else {
                // ❌ Use of Video Editor is restricted. License is revoked, expired, or invalid.
                // Displays fallback warning and disables editor buttons.
                Log.w(SampleApp.TAG, ERR_LICENSE_REVOKED)
                with(binding) {
                    licenseStateView.text = ERR_LICENSE_REVOKED
                    licenseStateView.visible()
                    btnVideoEditor.isEnabled = false
                    btnPiPVideoEditor.isEnabled = false
                    btnDraftsVideoEditor.isEnabled = false
                    btnSlideShowVideoEditorTrimmer.isEnabled = false
                    btnOpenPhotoEditor.isEnabled = false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun openVideoEditor(pipVideo: Uri = Uri.EMPTY) {
        // Step 5: Launch the Video Editor starting from camera recording flow
        // VideoCreationActivity orchestrates all video editor screens (Camera, Preview, Trimmer, etc.)
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
        startVideoEditor(videoCreationIntent)
    }

    private fun openVideoEditorDrafts() {
        // Launch Video Editor starting from saved drafts
        startVideoEditor(VideoCreationActivity.startFromDrafts(this))
    }

    private fun openVideoEditorTrimmerWithSlideShow(videos: List<Uri>) {
        // Launch Video Editor trimmer with slideshow images
        // Editor V2 is not available from Trimmer screen
        startVideoEditor(
            VideoCreationActivity.startFromTrimmer(
                this,
                videos.toTypedArray()
            )
        )
    }

    private fun openVideoEditorFromTemplates() {
        // Launch Video Editor starting from templates screen
        startVideoEditor(
            VideoCreationActivity.startFromTemplates(
                context = this
            )
        )
    }

    private fun startVideoEditor(intent: Intent) {
        // Ensure SDK is initialized and launch the video export result contract
        sampleApp.initSdk()
        videoEditorExportResult.launch(intent)
    }

    private fun startPhotoEditor(intent: Intent) {
        // Verify SDK initialization and launch Photo Editor export result contract
        val editor = sampleApp.editorSdk
        if (editor == null) {
            // Token you provided is not correct - empty or truncated
            Log.e(SampleApp.TAG, ERR_SDK_NOT_INITIALIZED)
        } else {
            photoEditorExportResult.launch(intent)
        }
    }
}
