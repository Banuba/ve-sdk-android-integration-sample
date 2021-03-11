package com.banuba.example.integrationapp.videoeditor.impl

import android.content.Context
import android.view.ViewGroup
import com.banuba.example.integrationapp.R
import com.banuba.example.integrationapp.videoeditor.widget.recordbutton.RecordButtonView
import com.banuba.sdk.cameraui.data.CameraRecordingAnimationProvider
import com.banuba.sdk.core.ui.ext.dimenPx

internal class IntegrationAppRecordingAnimationProvider(context: Context) : CameraRecordingAnimationProvider {

    private val animationView = RecordButtonView(context).apply {
        val defaultButtonSize = context.dimenPx(R.dimen.record_button_size)
        layoutParams = ViewGroup.LayoutParams(defaultButtonSize, defaultButtonSize)
        isFocusable = true
        isClickable = true
    }

    private var onEndTakenPictureAnimationCallback: () -> Unit = {}
    private var onStartRecordingAnimationCallback: () -> Unit = {}
    private var onEndRecordingAnimationCallback: (isAnimationFinished: Boolean) -> Unit = {}

    override fun provideView() = animationView

    override fun setOnStartRecordingAnimationCallback(callback: () -> Unit) {
        onStartRecordingAnimationCallback = callback
    }

    override fun setOnEndRecordingAnimationCallback(callback: (isAnimationFinished: Boolean) -> Unit) {
        onEndRecordingAnimationCallback = callback
    }

    override fun setRecordingProgress(progressMs: Long) {
        animationView.setRecordingProgress(progressMs)
    }

    override fun setOnEndTakenPictureAnimationCallback(callback: () -> Unit) {
        onEndTakenPictureAnimationCallback = callback
    }

    override fun animatePauseRecording() {
        animationView.pauseAnimation()
    }

    override fun animateResumeRecording() {
        animationView.resumeAnimation()
    }

    override fun setRecordingPhotoState(state: CameraRecordingAnimationProvider.PhotoState) {
        when (state) {
            CameraRecordingAnimationProvider.PhotoState.Idle -> animationView.reset()
            CameraRecordingAnimationProvider.PhotoState.Capture -> animationView.animateTakePhoto {
                onEndTakenPictureAnimationCallback()
            }
        }
    }

    override fun setRecordingVideoState(state: CameraRecordingAnimationProvider.VideoState) {
        when (state) {
            is CameraRecordingAnimationProvider.VideoState.Idle -> animationView.reset()
            is CameraRecordingAnimationProvider.VideoState.StartRecord -> {
                if (state.availableDurationMs <= 0) return
                animationView.animateStartVideoRecord(
                    availableDurationMs = state.availableDurationMs,
                    maxDurationMs = state.maxDurationMs
                ) {
                    onStartRecordingAnimationCallback()
                }
            }
            is CameraRecordingAnimationProvider.VideoState.StopRecord -> {
                animationView.animateStopVideoRecord {
                    onEndRecordingAnimationCallback(it)
                }
            }
        }
    }
}
