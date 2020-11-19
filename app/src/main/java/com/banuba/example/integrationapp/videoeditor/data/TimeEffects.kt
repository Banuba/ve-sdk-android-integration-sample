package com.banuba.example.integrationapp.videoeditor.data

import android.net.Uri
import com.banuba.example.integrationapp.R
import com.banuba.sdk.core.effects.IEffectDrawable
import com.banuba.sdk.core.ui.Speed
import com.banuba.sdk.effects.ve.time.speed.RapidEffect
import com.banuba.sdk.effects.ve.time.speed.SlowMotionEffect
import com.banuba.sdk.ve.effects.EditorEffectProvider

sealed class TimeEffects : EditorEffectProvider<IEffectDrawable> {

    override val previewUri: Uri = Uri.EMPTY

    data class SlowMo(val speed: Float = Speed.X05.value) : TimeEffects() {

        override val nameRes: Int
            get() = R.string.time_effect_slow_mo

        override val iconRes: Int
            get() = R.drawable.ic_effect

        override val colorRes: Int
            get() = R.color.orange

        override fun provide() = SlowMotionEffect(speed)
    }

    data class Rapid(val speed: Float = Speed.X2.value) : TimeEffects() {

        override val nameRes: Int
            get() = R.string.time_effect_rapid

        override val iconRes: Int
            get() = R.drawable.ic_effect

        override val colorRes: Int
            get() = R.color.blue

        override fun provide() = RapidEffect(speed)
    }
}
