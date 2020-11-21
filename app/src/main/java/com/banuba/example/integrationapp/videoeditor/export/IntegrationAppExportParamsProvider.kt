package com.banuba.example.integrationapp.videoeditor.export

import android.net.Uri
import androidx.core.net.toFile
import com.banuba.sdk.core.MediaSizeProvider
import com.banuba.sdk.core.VideoResolution
import com.banuba.sdk.core.media.MediaFileNameHelper.Companion.DEFAULT_SOUND_FORMAT
import com.banuba.sdk.ve.domain.VideoList
import com.banuba.sdk.ve.effects.Effects
import com.banuba.sdk.ve.effects.WatermarkAlignment
import com.banuba.sdk.ve.effects.WatermarkBuilder
import com.banuba.sdk.ve.player.MusicEffect
import com.banuba.sdk.ve.processing.ExportManager
import com.banuba.sdk.veui.data.ExportParamsProvider
import com.banuba.sdk.veui.ext.withWatermark

class IntegrationAppExportParamsProvider(
    private val exportDir: Uri,
    private val sizeProvider: MediaSizeProvider,
    private val watermarkBuilder: WatermarkBuilder
) : ExportParamsProvider {

    override fun provideExportParams(
        effects: Effects,
        videoList: VideoList,
        musicEffects: List<MusicEffect>,
        videoVolume: Float
    ): List<ExportManager.Params> {
        val exportSessionDir = exportDir.toFile().apply {
            deleteRecursively()
            mkdirs()
        }
        val extraSoundtrackUri = Uri.parse(exportSessionDir.toString()).buildUpon()
            .appendPath("exported_soundtrack.$DEFAULT_SOUND_FORMAT")
            .build()

        return listOf(
            ExportManager.Params.Builder(sizeProvider.provideOptimalExportVideoSize())
                .effects(effects.withWatermark(watermarkBuilder, WatermarkAlignment.BOTTOM_RIGHT))
                .fileName("export_default_watermark")
                .videoList(videoList)
                .destDir(exportSessionDir)
                .musicEffects(musicEffects)
                .extraAudioFile(extraSoundtrackUri)
                .volumeVideo(videoVolume)
                .build(),
            ExportManager.Params.Builder(sizeProvider.provideOptimalExportVideoSize())
                .effects(effects)
                .fileName("export_default")
                .videoList(videoList)
                .destDir(exportSessionDir)
                .musicEffects(musicEffects)
                .volumeVideo(videoVolume)
                .build(),
            ExportManager.Params.Builder(VideoResolution.VGA360.size)
                .effects(effects.withWatermark(watermarkBuilder, WatermarkAlignment.BOTTOM_RIGHT))
                .fileName("export_360_watermark")
                .videoList(videoList)
                .destDir(exportSessionDir)
                .musicEffects(musicEffects)
                .volumeVideo(videoVolume)
                .build()
        )
    }
}
