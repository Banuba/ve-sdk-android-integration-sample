package com.banuba.example.integrationapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.banuba.example.integrationapp.videoeditor.IntegrationAppExportVideoContract
import com.banuba.sdk.cameraui.data.PipConfig
import com.banuba.sdk.core.ui.ext.visible
import com.banuba.sdk.token.storage.provider.TokenProvider
import com.banuba.sdk.ve.flow.VideoCreationActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TOKEN_URL =
            "https://github.com/Banuba/ve-sdk-android-integration-sample#token"
    }

    private val tokenProvider: TokenProvider by inject(
        TokenProvider::class.java,
        named("banubaTokenProvider")
    )

    private val createVideoRequest =
        registerForActivityResult(IntegrationAppExportVideoContract()) { exportResult ->
            exportResult?.let {
                //handle ExportResult object
            }
        }

    private val createVideoWithPiPRequest = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        openVideoEditor(pipVideo = it ?: Uri.EMPTY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnVideoEditor.setOnClickListener {
            openVideoEditor()
        }
        btnPiPVideoEditor.setOnClickListener {
            createVideoWithPiPRequest.launch("video/*")
        }
        btnDraftsVideoEditor.setOnClickListener {
            openVideoEditorWithDrafts()
        }

        if (tokenProvider.getToken().isEmpty()) {
            infoTextView.text =
                "Video editor requires a token. \nPlease, follow the steps described in our Github: $TOKEN_URL "
            infoTextView.visible()
            btnVideoEditor.isEnabled = false
            btnPiPVideoEditor.isEnabled = false
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
        createVideoRequest.launch(videoCreationIntent)
    }

    private fun openVideoEditorWithDrafts() {
        createVideoRequest.launch(VideoCreationActivity.startFromDrafts(this))
    }
}
