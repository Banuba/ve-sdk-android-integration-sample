package com.banuba.example.integrationapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.banuba.sdk.cameraui.domain.MODE_RECORD_VIDEO
import com.banuba.sdk.ve.flow.VideoCreationActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnVideoEditor.setOnClickListener {
            val intent = VideoCreationActivity.buildIntent(
                context = this,
                // setup what kind of action you want to do with VideoCreationActivity
                mode = MODE_RECORD_VIDEO,
                // setup data that will be acceptable during export flow
                additionalExportData = null,
                // set TrackData object if you open VideoCreationActivity with preselected music track
                audioTrackData = null
            )
            startActivity(intent)
        }
    }
}