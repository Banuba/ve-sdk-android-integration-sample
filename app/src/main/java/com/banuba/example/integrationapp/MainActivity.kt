package com.banuba.example.integrationapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.banuba.example.integrationapp.videoeditor.IntegrationAppExportVideoContract
import com.banuba.sdk.ve.flow.VideoCreationActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val createVideoRequest =
        registerForActivityResult(IntegrationAppExportVideoContract()) { exportResult ->
            exportResult?.let {
                //handle ExportResult object
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnVideoEditor.setOnClickListener {
            createVideoRequest.launch(
                VideoCreationActivity.buildIntent(
                    context = this,
                    // setup what kind of action you want to do with VideoCreationActivity
                    // setup data that will be acceptable during export flow
                    additionalExportData = null,
                    // set TrackData object if you open VideoCreationActivity with preselected music track
                    audioTrackData = null
                )
            )
        }
    }
}
