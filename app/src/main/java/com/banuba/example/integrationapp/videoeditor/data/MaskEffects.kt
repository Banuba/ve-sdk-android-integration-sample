package com.banuba.example.integrationapp.videoeditor.data

import android.net.Uri
import com.banuba.example.integrationapp.R
import com.banuba.sdk.core.effects.IEffectDrawable
import com.banuba.sdk.effects.ve.visual.mask.MaskDrawable
import com.banuba.sdk.ve.effects.EditorEffectProvider

sealed class MaskEffects : EditorEffectProvider<IEffectDrawable> {

    abstract val path: Uri

    override val previewUri: Uri
        get() = path.buildUpon().appendEncodedPath("preview.png").build()

    override val iconRes: Int
        get() = R.drawable.ic_effect

    override fun provide() = MaskDrawable(path)

    data class AsaiLines(val effectsUri: Uri) : MaskEffects() {

        override val path: Uri =
            effectsUri.buildUpon().appendPath("AsaiLines").build()

        override val nameRes: Int
            get() = R.string.mask_effect_asailines

        override val colorRes: Int
            get() = R.color.cyan
    }

    data class HawaiiHairFlower(val effectsUri: Uri) : MaskEffects() {

        override val path: Uri =
            effectsUri.buildUpon().appendPath("HawaiiHairFlower").build()

        override val nameRes: Int
            get() = R.string.mask_effect_hawaiihairflower

        override val colorRes: Int
            get() = R.color.blue
    }
}
