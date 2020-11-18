package com.banuba.example.integrationapp.videoeditor.di

import android.app.Application
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.banuba.android.sdk.camera.BanubaCameraSdkManager
import com.banuba.android.sdk.camera.CameraSdkManager
import com.banuba.example.integrationapp.R
import com.banuba.example.integrationapp.videoeditor.export.IntegrationAppExportFlowManager
import com.banuba.example.integrationapp.videoeditor.export.IntegrationAppExportResultHandler
import com.banuba.example.integrationapp.videoeditor.impl.GlideImageLoader
import com.banuba.example.integrationapp.videoeditor.impl.IntegrationAppWatermarkProvider
import com.banuba.sdk.core.AREffectPlayerProvider
import com.banuba.sdk.core.IUtilityManager
import com.banuba.sdk.core.domain.ImageLoader
import com.banuba.sdk.core.effects.EffectsResourceManager
import com.banuba.sdk.effectplayer.adapter.BanubaAREffectPlayerProvider
import com.banuba.sdk.effectplayer.adapter.BanubaClassFactory
import com.banuba.sdk.ve.effects.WatermarkProvider
import com.banuba.sdk.ve.flow.ExportFlowManager
import com.banuba.sdk.ve.flow.ExportResultHandler
import com.banuba.sdk.ve.flow.FlowEditorModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.definition.BeanDefinition

/**
 * All dependencies mentioned in this module will override default
 * implementations provided from SDK.
 * Some dependencies has no default implementations. It means that
 * these classes fully depends on your requirements
 */
class VideoeditorKoinModule : FlowEditorModule() {

    override val effectPlayerManager: BeanDefinition<AREffectPlayerProvider> =
        single(override = true) {
            BanubaAREffectPlayerProvider(
                mediaSizeProvider = get(),
                token = androidContext().getString(R.string.video_editor_token)
            )
        }

    override val banubaSdkManager: BeanDefinition<CameraSdkManager> =
        single(override = true, createdAtStart = true) {
            val context = get<Application>().applicationContext

            val effectsResourceManager = EffectsResourceManager(
                assetManager = context.assets,
                storageDir = context.filesDir
            )
            val utilityManager: IUtilityManager = BanubaClassFactory.createUtilityManager(
                context, effectsResourceManager
            )
            val effectPlayerProvider: AREffectPlayerProvider = get()

            BanubaCameraSdkManager.createInstance(
                context,
                effectsResourceManager,
                effectPlayerProvider,
                utilityManager
            )
        }

    override val exportFlowManager: BeanDefinition<ExportFlowManager> = single {
        IntegrationAppExportFlowManager()
    }

    override val exportResultHandler: BeanDefinition<ExportResultHandler> = single {
        IntegrationAppExportResultHandler()
    }

    override val imageLoader: BeanDefinition<ImageLoader> =
        factory(override = true) { (source: Any) ->
            when (source) {
                is Fragment -> GlideImageLoader(fragment = source)
                is View -> GlideImageLoader(view = source)
                is Context -> GlideImageLoader(context = source)
                else -> throw IllegalArgumentException("Illegal source for GlideImageLoader")
            }
        }

    override val watermarkProvider: BeanDefinition<WatermarkProvider> = factory(override = true) {
        IntegrationAppWatermarkProvider()
    }
}