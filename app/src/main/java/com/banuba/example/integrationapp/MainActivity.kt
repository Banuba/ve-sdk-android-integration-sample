package com.banuba.example.integrationapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.banuba.example.integrationapp.databinding.ActivityMainBinding
import com.banuba.sdk.core.ui.ext.visible
import com.banuba.sdk.export.data.ExportResult
import com.banuba.sdk.export.utils.EXTRA_EXPORTED_SUCCESS
import com.banuba.sdk.pe.PhotoCreationActivity
import com.banuba.sdk.pe.PhotoExportResultContract
import com.banuba.sdk.ve.flow.VideoCreationActivity


class MainActivity : AppCompatActivity() {

    // Handle Video Editor export results
    private val videoEditorExportResult =
        registerForActivityResult(CustomExportResultVideoContract()) { exportResult ->
            (application as? SampleApp)?.releaseVideoEditor()
            // The dialog is used to demo export result in a various ways.
            // It is not required to copy and paste this approach to your project.
            AlertDialog.Builder(this).setMessage("Export result")
                .setPositiveButton("Play Video") { _, _ -> playExportedVideo(exportResult) }
                .setNegativeButton("Open Sharing") { _, _ -> openSharingActivity(exportResult) }
                .setNeutralButton("Close") { _, _ -> }.create().show()
        }

    private fun playExportedVideo(exportResult: ExportResult?) {
        if (exportResult is ExportResult.Success) {
            exportResult.videoList.firstOrNull()?.let {
                Utils.playExportedVideo(this@MainActivity, it.sourceUri)
            }
        }
    }

    private fun openSharingActivity(exportResult: ExportResult?) {
        if (exportResult is ExportResult.Success) {
            val resId = com.banuba.sdk.export.R.string.export_action_name
            val intent =
                Intent(getString(resId, packageName)).apply {
                    putExtra(EXTRA_EXPORTED_SUCCESS, exportResult)
                }
            startActivity(intent)
        }
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

    private val photoEditorExportResult =
        registerForActivityResult(PhotoExportResultContract()) { uri ->
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.setDataAndType(uri, "image/*")

            try {
                startActivity(intent)
            } catch (e: Exception) {
                Log.d(SampleApp.TAG, "Can't handle intent")
            }
        }

    private var _binding: ActivityMainBinding? = null

    private val binding: ActivityMainBinding
        get() = requireNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        setContentView(binding.root)

        // Handle Video Editor license flow
        val videoEditor = (application as SampleApp).videoEditor
        if (videoEditor == null) {
            binding.licenseStateView.visible()
            binding.licenseStateView.text = SampleApp.ERR_SDK_NOT_INITIALIZED
            return
        }

        // Might take up to 1 sec in the worst case.
        // Please optimize use of this function in your project to bring the best user experience
        videoEditor.getLicenseState { isValid ->
            if (isValid) {
                // ✅ License is active, all good
                // You can show button that opens Video Editor or
                // Start Video Editor right away
                binding.btnVideoEditor.setOnClickListener {
                    openVideoEditor()
                }
                binding.btnPiPVideoEditor.setOnClickListener {
                    requestVideoOpenPIP.launch("video/*")
                }
                binding.btnDraftsVideoEditor.setOnClickListener {
                    openVideoEditorDrafts()
                }
                binding.btnSlideShowVideoEditorTrimmer.setOnClickListener {
                    requestImageOpenTrimmer.launch("image/*")
                }
                binding.btnOpenPhotoEditor.setOnClickListener {
                    photoEditorExportResult.launch(PhotoCreationActivity.startFromGallery(this@MainActivity))
                }
            } else {
                // ❌ Use of Video Editor is restricted. License is revoked or expired.
                binding.licenseStateView.text = SampleApp.ERR_LICENSE_REVOKED
                Log.w(SampleApp.TAG, SampleApp.ERR_LICENSE_REVOKED)

                binding.licenseStateView.visible()
                binding.btnVideoEditor.isEnabled = false
                binding.btnPiPVideoEditor.isEnabled = false
                binding.btnDraftsVideoEditor.isEnabled = false
                binding.btnSlideShowVideoEditorTrimmer.isEnabled = false
                binding.btnOpenPhotoEditor.isEnabled = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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
        startVideoEditor(videoCreationIntent)
    }

    private fun openVideoEditorDrafts() {
        startVideoEditor(VideoCreationActivity.startFromDrafts(this))
    }

    private fun openVideoEditorTrimmerWithSlideShow(videos: List<Uri>) {
        startVideoEditor(
            VideoCreationActivity.startFromTrimmer(
                this,
                videos.toTypedArray()
            )
        )
    }

    private fun startVideoEditor(veIntent: Intent) {
        (application as? SampleApp)?.prepareVideoEditor()
        videoEditorExportResult.launch(veIntent)
    }
}
