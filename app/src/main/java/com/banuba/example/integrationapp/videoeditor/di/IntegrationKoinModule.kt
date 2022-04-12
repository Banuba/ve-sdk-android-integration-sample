package com.banuba.example.integrationapp.videoeditor.di

import android.app.Activity
import androidx.fragment.app.Fragment
import com.banuba.android.sdk.ve.timeline.`object`.data.ObjectEditorConfig
import com.banuba.example.integrationapp.MainActivity
import com.banuba.example.integrationapp.videoeditor.export.ExportVideoResolutionProvider
import com.banuba.example.integrationapp.videoeditor.export.IntegrationAppExportParamsProvider
import com.banuba.example.integrationapp.videoeditor.impl.*
import com.banuba.sdk.arcloud.data.source.ArEffectsRepositoryProvider
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
import com.banuba.sdk.export.data.ExportResult
import com.banuba.sdk.export.data.ForegroundExportFlowManager
import com.banuba.sdk.ve.effects.watermark.WatermarkProvider
import com.banuba.sdk.veui.data.EditorConfig
import com.banuba.sdk.veui.domain.CoverProvider
import com.banuba.sdk.veui.ui.sharing.SharingActionHandler
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
        single<ExportFlowManager> {
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
        factory<ExportParamsProvider> {
            IntegrationAppExportParamsProvider(
                exportDir = get(named("exportDir")),
                sizeProvider = get(),
                watermarkBuilder = get()
            )
        }

        factory<WatermarkProvider> {
            IntegrationAppWatermarkProvider()
        }

        factory<CameraTimerStateProvider> {
            IntegrationTimerStateProvider()
        }

        single<ArEffectsRepositoryProvider>(createdAtStart = true) {
            ArEffectsRepositoryProvider(
                arEffectsRepository = get(named("backendArEffectsRepository")),
                ioDispatcher = get(named("ioDispatcher"))
            )
        }

        single<ContentFeatureProvider<TrackData, Fragment>>(
            named("musicTrackProvider")
        ) {
            ExternalMusicProvider()
        }

        single<CoverProvider> {
            CoverProvider.EXTENDED
        }

        single<CameraTimerActionProvider> {
            HandsFreeTimerActionProvider()
        }

        single<OrderProvider>(named("colorFilterOrderProvider")) {
            IntegrationAppColorFilterOrderProvider()
        }

        single<OrderProvider>(named("maskOrderProvider")) {
            IntegrationAppMaskOrderProvider()
        }

        factory<DraftConfig> {
            DraftConfig.ENABLED_ASK_TO_SAVE
        }

        single<AspectRatioProvider> {
            object : AspectRatioProvider {
                override fun provide(): AspectRatio = AspectRatio(9.0 / 16)
            }
        }

        single<EditorConfig> {
            EditorConfig(
                minTotalVideoDurationMs = 1500
            )
        }

        single<ObjectEditorConfig> {
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

        single<CameraTimerUpdateProvider> {
            IntegrationCameraTimerUpdateProvider(
                audioPlayer = get(),
                context = get()
            )
        }

        single<SharingActionHandler> {
            object : SharingActionHandler {
                override fun onBack(activity: Activity) {
                    if (activity is MainActivity) {
                        activity.hideSharingScreen()
                    }
                }

                override fun onClose(activity: Activity, result: ExportResult.Success?) {
                    if (activity is MainActivity) {
                        activity.hideSharingScreen()
                    }
                }
            }
        }
    }
}
