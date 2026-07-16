package com.banuba.example.integrationapp

import android.net.Uri
import android.util.Size
import androidx.core.net.toFile
import com.banuba.sdk.core.AspectRatio
import com.banuba.sdk.core.VideoResolution
import com.banuba.sdk.export.data.ExportParams
import com.banuba.sdk.export.data.ExportParamsProvider
import com.banuba.sdk.ve.domain.VideoRangeList
import com.banuba.sdk.ve.effects.Effects
import com.banuba.sdk.ve.effects.music.MusicEffect
import com.banuba.sdk.ve.effects.watermark.WatermarkBuilder

class CustomExportParamsProvider(
    private val exportDir: Uri,
    private val watermarkBuilder: WatermarkBuilder,
) : ExportParamsProvider {

    override fun provideExportParams(
        effects: Effects,
        videoRangeList: VideoRangeList,
        musicEffects: List<MusicEffect>,
        videoVolume: Float,
    ): List<ExportParams> {
        val exportSessionDir = exportDir.toFile().apply {
            deleteRecursively()
            mkdirs()
        }

        val exportVideoHD = ExportParams(
            resolution = VideoResolution.Exact.QHD,
            aspectRatio= AspectRatio(16.0/9),
            effects = effects,
            videoRangeList = videoRangeList,
            debugEnabled = true,
            destDir = exportSessionDir,
            fileExt = ".mp4",
            fileName = "export_video_QHD",
            volumeVideo = videoVolume,
            musicEffects = musicEffects,
            extraAudioFile = Uri.EMPTY,
            interactivePreviewParams = null,
            size = Size(0, 0),
            useHevcIfPossible = true
        )
        return listOf(exportVideoHD)
    }
}