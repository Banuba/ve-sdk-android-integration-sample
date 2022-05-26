package com.banuba.example.integrationapp.videoeditor.di

import androidx.fragment.app.Fragment
import com.banuba.android.sdk.ve.timeline.`object`.data.ObjectEditorConfig
import com.banuba.example.integrationapp.videoeditor.export.ExportVideoResolutionProvider
import com.banuba.example.integrationapp.videoeditor.export.IntegrationAppExportParamsProvider
import com.banuba.example.integrationapp.videoeditor.impl.IntegrationAppColorFilterOrderProvider
import com.banuba.example.integrationapp.videoeditor.impl.IntegrationAppMaskOrderProvider
import com.banuba.example.integrationapp.videoeditor.impl.IntegrationAppWatermarkProvider
import com.banuba.example.integrationapp.videoeditor.impl.IntegrationCameraTimerUpdateProvider
import com.banuba.example.integrationapp.videoeditor.impl.IntegrationTimerStateProvider
import com.banuba.sdk.arcloud.data.source.ArEffectsRepositoryProvider
import com.banuba.sdk.audiobrowser.domain.AudioBrowserMusicProvider
import com.banuba.sdk.cameraui.data.CameraTimerActionProvider
import com.banuba.sdk.cameraui.data.CameraTimerStateProvider
import com.banuba.sdk.cameraui.data.CameraTimerUpdateProvider
import com.banuba.sdk.cameraui.domain.HandsFreeTimerActionProvider
import com.banuba.sdk.core.AspectRatio
import com.banuba.sdk.core.HardwareClassProvider
import com.banuba.sdk.core.VideoResolution
import com.banuba.sdk.core.data.OrderProvider
import com.banuba.sdk.core.data.TrackData
import com.banuba.sdk.core.domain.AspectRatioProvider
import com.banuba.sdk.core.domain.DraftConfig
import com.banuba.sdk.core.ui.ContentFeatureProvider
import com.banuba.sdk.export.data.ExportFlowManager
import com.banuba.sdk.export.data.ExportParamsProvider
import com.banuba.sdk.export.data.ForegroundExportFlowManager
import com.banuba.sdk.ve.effects.watermark.WatermarkProvider
import com.banuba.sdk.veui.data.EditorConfig
import com.banuba.sdk.veui.domain.CoverProvider
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * All dependencies mentioned in this module will override default
 * implementations provided from SDK.
 * Some dependencies has no default implementations. It means that
 * these classes fully depends on your requirements
 */
class IntegrationKoinModule {

    val module = module {
        single<ExportFlowManager>(override = true) {
            ForegroundExportFlowManager(
                exportDataProvider = get(),
                sessionParamsProvider = get(),
                exportSessionHelper = get(),
                exportDir = get(named("exportDir")),
                shouldClearSessionOnFinish = true,
                publishManager = get(),
                errorParser = get(),
                mediaFileNameHelper = get(),
                exportBundleProvider = get()
            )
        }

        /**
         * Provides params for export
         * */
        factory<ExportParamsProvider>(override = true) {
            IntegrationAppExportParamsProvider(
                exportDir = get(named("exportDir")),
                sizeProvider = get(),
                watermarkBuilder = get()
            )
        }

        factory<WatermarkProvider>(override = true) {
            IntegrationAppWatermarkProvider()
        }

        factory<CameraTimerStateProvider>(override = true) {
            IntegrationTimerStateProvider()
        }

        single<ArEffectsRepositoryProvider>(override = true, createdAtStart = true) {
            ArEffectsRepositoryProvider(
                arEffectsRepository = get(named("backendArEffectsRepository")),
                ioDispatcher = get(named("ioDispatcher"))
            )
        }

        single<ContentFeatureProvider<TrackData, Fragment>>(
            named("musicTrackProvider"),
            override = true
        ) {
            AudioBrowserMusicProvider()
        }

        single<CoverProvider>(override = true) {
            CoverProvider.EXTENDED
        }

        single<CameraTimerActionProvider>(override = true) {
            HandsFreeTimerActionProvider()
        }

        single<OrderProvider>(named("colorFilterOrderProvider"), override = true) {
            IntegrationAppColorFilterOrderProvider()
        }

        single<OrderProvider>(named("maskOrderProvider"), override = true) {
            IntegrationAppMaskOrderProvider()
        }

        factory<DraftConfig>(override = true) {
            DraftConfig.ENABLED_ASK_TO_SAVE
        }

        single<AspectRatioProvider>(override = true) {
            object : AspectRatioProvider {
                override fun provide(): AspectRatio = AspectRatio(9.0 / 16)
            }
        }

        single<EditorConfig>(override = true) {
            EditorConfig(
                minTotalVideoDurationMs = 1500
            )
        }

        single<ObjectEditorConfig>(override = true) {
            ObjectEditorConfig(
                objectEffectDefaultDuration = 2000
            )
        }

        single<ExportVideoResolutionProvider> {
            val hardwareClass = get<HardwareClassProvider>().provideHardwareClass()
            object : ExportVideoResolutionProvider {
                override var videoResolution: VideoResolution = hardwareClass.optimalResolution
            }
        }

        single<CameraTimerUpdateProvider>(override = true) {
            IntegrationCameraTimerUpdateProvider(
                audioPlayer = get(),
                context = get()
            )
        }
    }
}
