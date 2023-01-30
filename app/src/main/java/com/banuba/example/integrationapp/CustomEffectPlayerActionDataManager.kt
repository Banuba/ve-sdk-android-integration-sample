package com.banuba.example.integrationapp

import android.content.Context
import com.banuba.sdk.arcloud.data.source.model.ArEffect
import com.banuba.sdk.core.domain.BackgroundSeparationActionDataProvider
import com.banuba.sdk.core.domain.DefaultActionDataProvider
import com.banuba.sdk.core.domain.EffectPlayerActionDataManager
import com.banuba.sdk.core.domain.EffectPlayerActionDataProvider
import com.banuba.sdk.core.domain.ImageConverter
import com.banuba.sdk.core.domain.ScannerActionDataProvider
import com.banuba.sdk.core.effects.ArEffectInfo
import com.banuba.sdk.core.media.ReleasableObject
import com.banuba.sdk.token.storage.license.BanubaLicenseManager

class CustomEffectPlayerActionDataManager(
    private val context: Context,
    private val storageEffectsDir: String,
    private val imageConverter: ImageConverter
) : EffectPlayerActionDataManager {

    private var currentProvider: EffectPlayerActionDataProvider? = null

    override fun createProvider(arEffectInfo: ArEffectInfo): EffectPlayerActionDataProvider {
        release()
        return when {
            arEffectInfo.path.substringAfterLast("/") == ScannerActionDataProvider.EFFECT_NAME ->
                ScannerActionDataProvider(
                    arEffectInfo.copy(withAutoStop = true)
                )
            arEffectInfo.path.substringAfterLast("/") == GameActionDataProvider.EFFECT_NAME ->
                GameActionDataProvider(
                    arEffectInfo.copy(withAutoStop = true)
                )
            arEffectInfo.path.substringAfterLast("/") == BackgroundSeparationActionDataProvider.EFFECT_NAME ->
                BackgroundSeparationActionDataProvider(
                    context,
                    arEffectInfo,
                    storageEffectsDir,
                    imageConverter
                )
            else -> DefaultActionDataProvider(arEffectInfo)
        }.apply {
            currentProvider = this
        }
    }

    override fun isEffectAvailable(effect: ArEffect): Boolean =
        when (effect.name) {
            BackgroundSeparationActionDataProvider.EFFECT_NAME ->
                BanubaLicenseManager.getLicense().supportsBgSeparation
            else -> true
        }

    override fun release() {
        when (currentProvider) {
            is ReleasableObject -> (currentProvider as ReleasableObject).release()
        }
        currentProvider = null
    }
}
