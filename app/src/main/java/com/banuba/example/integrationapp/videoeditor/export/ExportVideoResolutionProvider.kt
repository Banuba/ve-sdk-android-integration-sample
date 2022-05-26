package com.banuba.example.integrationapp.videoeditor.export

import com.banuba.sdk.core.VideoResolution

/**
 * Interface configures exported video resolution
 */
interface ExportVideoResolutionProvider {
    var videoResolution: VideoResolution
}