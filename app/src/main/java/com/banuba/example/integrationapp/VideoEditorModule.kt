package com.banuba.example.integrationapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.banuba.sdk.arcloud.data.source.ArEffectsRepositoryProvider
import com.banuba.sdk.arcloud.di.ArCloudKoinModule
import com.banuba.sdk.audiobrowser.di.AudioBrowserKoinModule
import com.banuba.sdk.audiobrowser.domain.AudioBrowserMusicProvider
import com.banuba.sdk.core.data.TrackData
import com.banuba.sdk.core.ui.ContentFeatureProvider
import com.banuba.sdk.effectplayer.adapter.BanubaEffectPlayerKoinModule
import com.banuba.sdk.export.di.VeExportKoinModule
import com.banuba.sdk.gallery.di.GalleryKoinModule
import com.banuba.sdk.playback.di.VePlaybackSdkKoinModule
import com.banuba.sdk.ve.data.EditorAspectSettings
import com.banuba.sdk.ve.data.aspect.AspectSettings
import com.banuba.sdk.ve.data.aspect.AspectsProvider
import com.banuba.sdk.ve.di.VeSdkKoinModule
import com.banuba.sdk.ve.flow.di.VeFlowKoinModule
import com.banuba.sdk.veui.data.EditorConfig
import com.banuba.sdk.veui.di.VeUiSdkKoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

class VideoEditorModule {

    fun initialize(applicationContext: Context) {
        startKoin {
            androidContext(applicationContext)
            allowOverride(true)

            // pass the customized Koin modules that implements required dependencies. Keep order of modules
            modules(
                VeSdkKoinModule().module,
                VeExportKoinModule().module,
                VePlaybackSdkKoinModule().module,
                AudioBrowserKoinModule().module,
                ArCloudKoinModule().module,
                VeUiSdkKoinModule().module,
                VeFlowKoinModule().module,
                GalleryKoinModule().module,
                BanubaEffectPlayerKoinModule().module,
                SampleIntegrationKoinModule().module,
            )
        }
    }
}

/**
 * All dependencies mentioned in this module will override default
 * implementations provided from SDK.
 */
private class SampleIntegrationKoinModule {

    val module = module {
        single<ArEffectsRepositoryProvider>(createdAtStart = true) {
            ArEffectsRepositoryProvider(
                arEffectsRepository = get(named("backendArEffectsRepository")),
                ioDispatcher = get(named("ioDispatcher"))
            )
        }

        single<ContentFeatureProvider<TrackData, Fragment>>(
            named("musicTrackProvider")
        ) {
            AudioBrowserMusicProvider()
        }

        single {
            object : AspectsProvider {

                override var availableAspects: List<AspectSettings> = listOf(
                    EditorAspectSettings.`16_9`
                )

                override fun provide(): AspectsProvider.AspectsData {
                    return AspectsProvider.AspectsData(
                        allAspects = availableAspects,
                        default = availableAspects.first()
                    )
                }
                override fun setBundle(bundle: Bundle) {}
            }
        }

        single {
            EditorConfig(
                aspectSettings = EditorAspectSettings.`16_9`
            )
        }
    }
}
