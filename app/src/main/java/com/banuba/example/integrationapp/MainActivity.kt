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
import com.banuba.example.integrationapp.databinding.ActivityMainBinding
import com.banuba.sdk.cameraui.data.PipConfig
import com.banuba.sdk.core.ui.ext.visible
import com.banuba.sdk.effectplayer.adapter.BanubaSdkResourcesDownloader
import com.banuba.sdk.export.data.ExportResult
//import com.banuba.sdk.pe.PhotoCreationActivity
//import com.banuba.sdk.pe.PhotoExportResultContract
import com.banuba.sdk.ve.flow.VideoCreationActivity
import com.banuba.sdk.ve.flow.VideoExportResultContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    // Handle Video Editor export results
    private val videoEditorExportResult =
        registerForActivityResult(VideoExportResultContract()) { exportResult ->
            // The dialog is used only to demonstrate and play an exported video file.

            // Release Video Editor SDK after exporting video and closing the latest SDK screen
            (application as? SampleApp)?.releaseVideoEditor()

            AlertDialog.Builder(this).setMessage("Export result")
                .setPositiveButton("Play Video") { _, _ ->
                    playExportedVideo(exportResult)
                }
                .setNeutralButton("Close") { _, _ ->
                }.create().show()
        }

    private fun playExportedVideo(exportResult: ExportResult?) {
        if (exportResult is ExportResult.Success) {
            exportResult.videoList.firstOrNull()?.let {
                Utils.playExportedVideo(this@MainActivity, it.sourceUri)
            }
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

    private val requestImageOpenPhotoEditor = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { imageUri ->
        if (imageUri == null || imageUri == Uri.EMPTY) {
            Log.w(SampleApp.TAG, "Please pick image to open Photo Editor SDK!")
        } else {
            /*photoEditorExportResult.launch(
                PhotoCreationActivity.startFromEditor(
                    applicationContext,
                    imageUri = imageUri
                )
            )*/
        }
    }

    /*private val photoEditorExportResult =
        registerForActivityResult(PhotoExportResultContract()) { uri ->
            if (uri == null || uri == Uri.EMPTY) {
                val errMessage =
                    "No exported image or the token does not support Photo Editor SDK"
                Log.w(SampleApp.TAG, errMessage)
                Toast.makeText(applicationContext, errMessage, Toast.LENGTH_SHORT).show()
                return@registerForActivityResult
            }

            Utils.previewExportedImage(this, uri)
        }*/

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
                    // Start Photo Editor SDK
                    // photoEditorExportResult.launch(PhotoCreationActivity.startFromGallery(this@MainActivity))
                }

                binding.btnOpenPhotoEditorImage.setOnClickListener {
                    // Start Photo Editor SDK
                    requestImageOpenPhotoEditor.launch("image/*")
                }


                // WARNING! EXPERIMENTAL FEATURE
                downloadFarSdkResources()
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


    private fun downloadFarSdkResources() {
        val downloadFarResourcesUrl = PUT DOWNLOAD ZIP URL
        val job = lifecycleScope.launchWhenCreated {
            withContext(Dispatchers.IO) {
                BanubaSdkResourcesDownloader.download(
                    applicationContext.filesDir,
                    downloadFarResourcesUrl
                )
            }
        }
        job.start()
    }
}
