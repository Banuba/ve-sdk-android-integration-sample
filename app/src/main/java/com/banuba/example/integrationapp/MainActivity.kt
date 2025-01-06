package com.banuba.example.integrationapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.banuba.example.integrationapp.SampleApp.Companion.ERR_LICENSE_REVOKED
import com.banuba.example.integrationapp.SampleApp.Companion.ERR_SDK_NOT_INITIALIZED
import com.banuba.example.integrationapp.databinding.ActivityMainBinding
import com.banuba.sdk.core.ui.ext.visible
import com.banuba.sdk.pe.BanubaPhotoEditor
import com.banuba.sdk.pe.PhotoCreationActivity
import com.banuba.sdk.pe.PhotoExportResultContract


class MainActivity : AppCompatActivity() {

    private val requestImageOpenPhotoEditor = registerForActivityResult(
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
            BanubaPhotoEditor.release()

            if (uri == null || uri == Uri.EMPTY) {
                val errMessage =
                    "No exported image or the token does not support Photo Editor SDK"
                Log.w(SampleApp.TAG, errMessage)
                Toast.makeText(applicationContext, errMessage, Toast.LENGTH_SHORT).show()
                return@registerForActivityResult
            }

            Log.d(SampleApp.TAG, "Exported image uri = $uri")

            Utils.previewExportedImage(this, uri)
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

        // Handle Video Editor license flow
        val photoEditor = sampleApp.photoEditor
        if (photoEditor == null) {
            binding.licenseStateView.visible()
            binding.licenseStateView.text = ERR_SDK_NOT_INITIALIZED
            return
        }

        // Might take up to 1 sec in the worst case.
        // Please optimize use of this function in your project to bring the best user experience
        photoEditor.getLicenseState { isValid ->
            if (isValid) {
                // ✅ License is active, all good
                binding.btnOpenPhotoEditor.setOnClickListener {
                    // Start Photo Editor SDK
                    startPhotoEditor(PhotoCreationActivity.startFromGallery(this@MainActivity))
                }

                binding.btnOpenPhotoEditorImage.setOnClickListener {
                    // Start Photo Editor SDK
                    requestImageOpenPhotoEditor.launch("image/*")
                }
            } else {
                // ❌ Use of Video Editor is restricted. License is revoked or expired.
                binding.licenseStateView.text = ERR_LICENSE_REVOKED
                Log.w(SampleApp.TAG, ERR_LICENSE_REVOKED)

                binding.btnOpenPhotoEditor.isEnabled = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun startPhotoEditor(intent: Intent) {
        sampleApp.preparePhotoEditor()

        val editor = sampleApp.photoEditor
        if (editor == null) {
            // Token you provided is not correct - empty or truncated
            Log.e(SampleApp.TAG, ERR_SDK_NOT_INITIALIZED)
        } else {
            photoEditorExportResult.launch(intent)
        }
    }
}
