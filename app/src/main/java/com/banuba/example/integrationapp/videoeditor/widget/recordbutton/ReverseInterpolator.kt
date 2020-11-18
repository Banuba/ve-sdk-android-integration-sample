package com.banuba.example.integrationapp.videoeditor.widget.recordbutton

import android.view.animation.Interpolator
import kotlin.math.abs

internal object ReverseInterpolator : Interpolator {

    override fun getInterpolation(input: Float) = abs(input - 1f)
}
