package com.banuba.example.integrationapp


import com.banuba.sdk.core.domain.ArEffectActionParams
import com.banuba.sdk.core.effects.ArEffectInfo
import com.banuba.sdk.core.domain.EffectPlayerActionDataProvider


class GameActionDataProvider(
    override val effectInfo: ArEffectInfo
) : EffectPlayerActionDataProvider() {

    companion object {
        const val EFFECT_NAME = "Game_orig"
        private const val METHOD_GAME_START = "onRecordStart()"
        private const val METHOD_GAME_END = "onRecordStop()"
    }

    private val gameActionStart by lazy(LazyThreadSafetyMode.NONE) {
        listOf(METHOD_GAME_START)
    }

    private val gameActionEnd by lazy(LazyThreadSafetyMode.NONE) {
        listOf(METHOD_GAME_END)
    }

    override fun doOnStartRecording(params: ArEffectActionParams) = gameActionStart

    override fun doOnStopRecording(params: ArEffectActionParams) = gameActionEnd
}
