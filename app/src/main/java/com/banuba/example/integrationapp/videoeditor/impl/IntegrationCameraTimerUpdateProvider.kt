package com.banuba.example.integrationapp.videoeditor.impl

import android.content.Context
import androidx.core.net.toUri
import com.banuba.sdk.cameraui.data.CameraTimerUpdateProvider
import com.banuba.sdk.core.data.AudioPlayer
import com.banuba.sdk.core.ext.copyFile
import java.io.File

class IntegrationCameraTimerUpdateProvider(
    private val audioPlayer: AudioPlayer,
    context: Context
) : CameraTimerUpdateProvider {

    companion object {
        private const val SOUND_FILE_NAME = "countdown1.wav"
    }

    private val soundFile = File(context.filesDir, SOUND_FILE_NAME)

    init {
        context.assets.copyFile(SOUND_FILE_NAME, soundFile)
    }

    override fun start() {
        audioPlayer.prepareTrack(soundFile.toUri())
        audioPlayer.play(false, 0L)
    }

    override fun update() {
        audioPlayer.play(false, 0L)
    }

    override fun finish() {
        audioPlayer.release()
    }
}
