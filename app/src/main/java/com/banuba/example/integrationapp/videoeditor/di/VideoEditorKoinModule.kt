package com.banuba.example.integrationapp.videoeditor.di

import androidx.fragment.app.Fragment
import com.banuba.example.integrationapp.videoeditor.export.IntegrationAppExportParamsProvider
import com.banuba.example.integrationapp.videoeditor.impl.IntegrationAppColorFilterOrderProvider
import com.banuba.example.integrationapp.videoeditor.impl.IntegrationAppWatermarkProvider
import com.banuba.example.integrationapp.videoeditor.impl.IntegrationTimerStateProvider
import com.banuba.sdk.arcloud.data.source.ArEffectsRepositoryProvider
import com.banuba.sdk.audiobrowser.domain.AudioBrowserMusicProvider
import com.banuba.sdk.cameraui.data.CameraTimerActionProvider
import com.banuba.sdk.cameraui.data.CameraTimerStateProvider
import com.banuba.sdk.cameraui.domain.HandsFreeTimerActionProvider
import com.banuba.sdk.core.data.ColorFilterOrderProvider
import com.banuba.sdk.core.domain.DraftConfig
import com.banuba.sdk.core.domain.TrackData
import com.banuba.sdk.core.ui.ContentFeatureProvider
import com.banuba.sdk.ve.effects.WatermarkProvider
import com.banuba.sdk.ve.flow.ExportFlowManager
import com.banuba.sdk.ve.flow.FlowEditorModule
import com.banuba.sdk.ve.flow.export.ForegroundExportFlowManager
import com.banuba.sdk.veui.data.ExportParamsProvider
import com.banuba.sdk.veui.domain.CoverProvider
import org.koin.core.definition.BeanDefinition
import org.koin.core.qualifier.named

/**
 * All dependencies mentioned in this module will override default
 * implementations provided from SDK.
 * Some dependencies has no default implementations. It means that
 * these classes fully depends on your requirements
 */
class VideoEditorKoinModule : FlowEditorModule() {

    override val exportFlowManager: BeanDefinition<ExportFlowManager> = single(override = true) {
        ForegroundExportFlowManager(
            exportDataProvider = get(),
            editorSessionHelper = get(),
            exportDir = get(named("exportDir")),
            mediaFileNameHelper = get(),
            shouldClearSessionOnFinish = true,
            publishManager = get(),
            draftManager = get()
        )
    }

    /**
     * Provides params for export
     * */
    override val exportParamsProvider: BeanDefinition<ExportParamsProvider> =
        factory(override = true) {
            IntegrationAppExportParamsProvider(
                exportDir = get(named("exportDir")),
                sizeProvider = get(),
                watermarkBuilder = get()
            )
        }

    override val watermarkProvider: BeanDefinition<WatermarkProvider> = factory(override = true) {
        IntegrationAppWatermarkProvider()
    }

    override val cameraTimerStateProvider: BeanDefinition<CameraTimerStateProvider> =
        factory(override = true) {
            IntegrationTimerStateProvider()
        }

    val arEffectsRepositoryProvider: BeanDefinition<ArEffectsRepositoryProvider> =
        single(override = true, createdAtStart = true) {
            ArEffectsRepositoryProvider(
                arEffectsRepository = get(named("backendArEffectsRepository")),
                ioDispatcher = get(named("ioDispatcher"))
            )
        }

    override val musicTrackProvider: BeanDefinition<ContentFeatureProvider<TrackData, Fragment>> =
        single(named("musicTrackProvider"), override = true) {
            AudioBrowserMusicProvider()
        }

    override val coverProvider: BeanDefinition<CoverProvider> = single(override = true) {
        CoverProvider.EXTENDED
    }

    override val cameraTimerActionProvider: BeanDefinition<CameraTimerActionProvider> =
        single(override = true) {
            HandsFreeTimerActionProvider()
        }

    override val colorFilterOrderProvider: BeanDefinition<ColorFilterOrderProvider> =
        single(override = true) {
            IntegrationAppColorFilterOrderProvider()
        }

    override val draftConfig: BeanDefinition<DraftConfig> = factory(override = true) {
        DraftConfig.ENABLED_ASK_TO_SAVE
    }
}
