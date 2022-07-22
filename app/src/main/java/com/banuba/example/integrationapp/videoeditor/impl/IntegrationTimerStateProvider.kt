package com.banuba.example.integrationapp.videoeditor.impl

import com.banuba.sdk.cameraui.data.CameraTimerStateProvider
import com.banuba.sdk.cameraui.data.TimerEntry

internal class IntegrationTimerStateProvider : CameraTimerStateProvider {

    override val timerStates = listOf(
            TimerEntry(
                    durationMs = 0
            ),
            TimerEntry(
                    durationMs = 3000
            )
    )
}