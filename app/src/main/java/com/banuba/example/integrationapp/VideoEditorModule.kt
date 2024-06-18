package com.banuba.example.integrationapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.banuba.sdk.arcloud.data.source.ArEffectsRepositoryProvider
import com.banuba.sdk.arcloud.di.ArCloudKoinModule
import com.banuba.sdk.audiobrowser.di.AudioBrowserKoinModule
import com.banuba.sdk.audiobrowser.domain.AudioBrowserMusicProvider
import com.banuba.sdk.core.data.TrackData
import com.banuba.sdk.core.domain.MediaNavigationProcessor
import com.banuba.sdk.core.ui.ContentFeatureProvider
import com.banuba.sdk.effectplayer.adapter.BanubaEffectPlayerKoinModule
import com.banuba.sdk.export.data.ExportResult
import com.banuba.sdk.export.di.VeExportKoinModule
import com.banuba.sdk.gallery.di.GalleryKoinModule
import com.banuba.sdk.playback.di.VePlaybackSdkKoinModule
import com.banuba.sdk.ve.di.VeSdkKoinModule
import com.banuba.sdk.ve.flow.VideoCreationActivity
import com.banuba.sdk.ve.flow.di.VeFlowKoinModule
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

        single<MediaNavigationProcessor> {
            object : MediaNavigationProcessor {
                override fun process(activity: Activity, mediaList: List<Uri>): Boolean {
                    val pngs = mediaList.filter { it.path?.contains(".png") ?: false }
                    return if (pngs.isEmpty()) {
                        true
                    } else {
                        (activity as? VideoCreationActivity)?.closeWithResult(
                            ExportResult.Success(
                                emptyList(),
                                pngs.first(),
                                Uri.EMPTY,
                                Bundle()
                            )
                        )
                        false
                    }
                }
            }
        }
    }
}
