package com.banuba.example.integrationapp.videoeditor.music

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.banuba.example.integrationapp.R
import com.banuba.sdk.audiobrowser.ui.AudioBrowserArgs
import com.banuba.sdk.audiobrowser.ui.AudioBrowserBottomSheet
import com.banuba.sdk.core.data.TrackData
import com.banuba.sdk.core.domain.ProvideTrackContract
import com.banuba.sdk.core.domain.ProvideTrackContract.Companion.EXTRA_RESULT_TRACK_DATA
import com.banuba.sdk.core.ui.OnMusicTrackHandler
import kotlinx.android.synthetic.main.fragment_custom_audio_browser.*

class CustomAudioBrowserActivity : AppCompatActivity(R.layout.fragment_custom_audio_browser),
    OnMusicTrackHandler {

    companion object {

        fun buildIntent(
            context: Context,
        ) = Intent(context, CustomAudioBrowserActivity::class.java)
    }

    private var selectedTrackData: TrackData? = null

    private val params by lazy(LazyThreadSafetyMode.NONE) {
        intent.extras?.let { ProvideTrackContract.obtainParams(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tvLastTrack.text = params?.lastProvidedTrackData?.title
        btnMubert.setOnClickListener {
            AudioBrowserBottomSheet.newInstance(
                AudioBrowserArgs(minTrackDurationMs = 1000L)
            ).show(
                supportFragmentManager, AudioBrowserBottomSheet.TAG
            )
        }
        btnApplyTrack.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra(EXTRA_RESULT_TRACK_DATA, selectedTrackData)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        btnResetTrack.setOnClickListener {
            selectedTrackData = null
            tvLastTrack.text = ""
        }
    }

    override fun handleMusicTrack(trackData: TrackData?) {
        selectedTrackData = trackData
        tvLastTrack.text = trackData?.title
    }
}
