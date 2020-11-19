package com.banuba.example.integrationapp.videoeditor.data

import android.net.Uri
import com.banuba.example.integrationapp.R
import com.banuba.sdk.core.effects.IEffectDrawable
import com.banuba.sdk.effects.ve.visual.rave.RaveDrawable
import com.banuba.sdk.effects.ve.visual.vhs.VHSDrawable
import com.banuba.sdk.ve.effects.EditorEffectProvider

sealed class VisualEffects : EditorEffectProvider<IEffectDrawable> {

    override val previewUri: Uri = Uri.EMPTY

    object VHS : VisualEffects() {

        override val nameRes: Int
            get() = R.string.visual_effect_vhs

        override val iconRes: Int
            get() = R.drawable.ic_effect

        override val colorRes: Int
            get() = R.color.greenNeon

        override fun provide() = VHSDrawable()
    }

    object Rave : VisualEffects() {

        override val nameRes: Int
            get() = R.string.visual_effect_rave

        override val iconRes: Int
            get() = R.drawable.ic_effect

        override val colorRes: Int
            get() = R.color.red

        override fun provide() = RaveDrawable()
    }
}
