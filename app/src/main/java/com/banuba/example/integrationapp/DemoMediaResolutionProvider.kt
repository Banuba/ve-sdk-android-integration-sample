package com.banuba.example.integrationapp

import com.banuba.sdk.core.MediaResolutionProvider
import com.banuba.sdk.core.VideoResolution
import com.banuba.sdk.core.HardwareClassProvider

class DemoMediaResolutionProvider(
    private val hardwareClassProvider: HardwareClassProvider
) : MediaResolutionProvider {

    private val hardwareClass = hardwareClassProvider.provideHardwareClass()

    override fun provideOptimalCameraPreviewSize(): VideoResolution.Exact {
        // Returning required video resolution as requested
        return VideoResolution.Exact.VGA480
    }

    override fun provideOptimalSlideShowVideoSize() = hardwareClass.optimalResolution

    override fun provideOptimalTrimmerVideoSize() = hardwareClass.optimalResolution

    override fun provideOptimalEditorVideoSize() = hardwareClass.optimalResolution

    override fun provideMaxFastTrimmerVideoSize() = hardwareClass.optimalResolution
}