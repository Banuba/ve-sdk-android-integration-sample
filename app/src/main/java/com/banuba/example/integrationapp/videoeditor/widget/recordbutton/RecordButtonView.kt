package com.banuba.example.integrationapp.videoeditor.widget.recordbutton

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.banuba.example.integrationapp.R
import com.banuba.sdk.core.ui.ext.dimenPx

internal class RecordButtonView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttrs) {

    companion object {

        private const val DEFAULT_DURATION_MS = 300L
        private const val FINISH_RECORD_DELAY = 200L

        private const val PHOTO_SHOOT_ANIMATION_SCALE_START = 1F
        private const val PHOTO_SHOOT_ANIMATION_SCALE_END = 0.875F

        private const val VIDEO_SHOOT_INNER_ANIMATION_SCALE_START = 1F
        private const val VIDEO_SHOOT_INNER_ANIMATION_SCALE_END = 0F

        private const val VIDEO_SHOOT_INNER_ANIMATION_REPEAT_COUNT = 0

        private const val PHOTO_SHOOT_OUTER_ANIMATION_START = 1F
        private const val PHOTO_SHOOT_OUTER_ANIMATION_END = 1.5F
    }

    private val outerPart = RecordButtonOuterView(context)

    private val innerOvalPart = View(context).apply {
        setBackgroundResource(R.drawable.ic_record_button_inner_oval)
        elevation = context.dimenPx(R.dimen.record_button_elevation).toFloat()
    }
    private val innerSquarePart = View(context).apply {
        val size = context.dimenPx(R.dimen.record_button_inner_part_size)
        layoutParams = LayoutParams(size, size).apply {
            gravity = Gravity.CENTER
        }
        setBackgroundResource(R.drawable.ic_record_button_inner_square)
        elevation = context.dimenPx(R.dimen.record_button_elevation).toFloat()
    }

    private val photoShootAnimation = ObjectAnimator.ofPropertyValuesHolder(
        innerOvalPart,
        PropertyValuesHolder.ofFloat(
            "scaleX",
            PHOTO_SHOOT_ANIMATION_SCALE_START,
            PHOTO_SHOOT_ANIMATION_SCALE_END
        ),
        PropertyValuesHolder.ofFloat(
            "scaleY",
            PHOTO_SHOOT_ANIMATION_SCALE_START,
            PHOTO_SHOOT_ANIMATION_SCALE_END
        )
    ).apply {
        duration = DEFAULT_DURATION_MS
        repeatCount = 1
        repeatMode = ValueAnimator.REVERSE
    }

    private val videoShootInnerAnimation = AnimatorSet().apply {
        val ovalAnim = ObjectAnimator.ofPropertyValuesHolder(
            innerOvalPart,
            PropertyValuesHolder.ofFloat(
                "scaleX",
                VIDEO_SHOOT_INNER_ANIMATION_SCALE_START,
                VIDEO_SHOOT_INNER_ANIMATION_SCALE_END
            ),
            PropertyValuesHolder.ofFloat(
                "scaleY",
                VIDEO_SHOOT_INNER_ANIMATION_SCALE_START,
                VIDEO_SHOOT_INNER_ANIMATION_SCALE_END
            )
        ).apply {
            duration = DEFAULT_DURATION_MS
            repeatCount = VIDEO_SHOOT_INNER_ANIMATION_REPEAT_COUNT
        }
        val squareAnim = ObjectAnimator.ofPropertyValuesHolder(
            innerSquarePart,
            PropertyValuesHolder.ofFloat(
                "scaleX",
                VIDEO_SHOOT_INNER_ANIMATION_SCALE_END,
                VIDEO_SHOOT_INNER_ANIMATION_SCALE_START
            ),
            PropertyValuesHolder.ofFloat(
                "scaleY",
                VIDEO_SHOOT_INNER_ANIMATION_SCALE_END,
                VIDEO_SHOOT_INNER_ANIMATION_SCALE_START
            )
        ).apply {
            duration = DEFAULT_DURATION_MS
            repeatCount = VIDEO_SHOOT_INNER_ANIMATION_REPEAT_COUNT
        }
        playTogether(ovalAnim, squareAnim)
    }
    private val videoShootOuterAnimation = AnimatorSet().apply {
        val circleAnim = ObjectAnimator.ofPropertyValuesHolder(
            outerPart,
            PropertyValuesHolder.ofFloat(
                "scaleX",
                PHOTO_SHOOT_OUTER_ANIMATION_START,
                PHOTO_SHOOT_OUTER_ANIMATION_END
            ),
            PropertyValuesHolder.ofFloat(
                "scaleY",
                PHOTO_SHOOT_OUTER_ANIMATION_START,
                PHOTO_SHOOT_OUTER_ANIMATION_END
            )
        ).apply {
            duration = DEFAULT_DURATION_MS
            repeatCount = 0
        }
        playTogether(circleAnim)
    }

    init {
        val outerPartSize = context.dimenPx(R.dimen.record_button_progress_size)
        addView(outerPart, LayoutParams(outerPartSize, outerPartSize).apply {
            gravity = Gravity.CENTER
        })
        addView(innerSquarePart)
        addView(innerOvalPart)
    }

    fun reset() {
        photoShootAnimation.cancel()
        videoShootInnerAnimation.cancel()
    }

    fun animateTakePhoto(onEndCallback: () -> Unit) {
        with(photoShootAnimation) {
            doOnEnd { onEndCallback() }
            start()
        }
    }

    fun animateStartVideoRecord(
        availableDurationMs: Long,
        maxDurationMs: Long,
        onEndCallback: () -> Unit
    ) {
        outerPart.startAnimation(
            availableDurationMs = availableDurationMs,
            maxDurationMs = maxDurationMs
        )
        with(videoShootInnerAnimation) {
            interpolator = LinearInterpolator()
            removeAllListeners()
            doOnEnd { onEndCallback() }
            start()
        }
        with(videoShootOuterAnimation) {
            interpolator = LinearInterpolator()
            start()
        }
    }

    fun animateStopVideoRecord(onEndCallback: (isAnimationFinished: Boolean) -> Unit) {
        outerPart.stopAnimation()
        with(videoShootInnerAnimation) {
            interpolator = ReverseInterpolator
            removeAllListeners()
            doOnStart {
                onEndCallback(false)
            }
            doOnEnd {
                onEndCallback(true)
            }
            start()
        }
        with(videoShootOuterAnimation) {
            interpolator = ReverseInterpolator
            start()
        }
    }

    fun pauseAnimation() {
        outerPart.pauseAnimation()
    }

    fun resumeAnimation() {
        outerPart.resumeAnimation()
    }

    fun setRecordingProgress(progressMs: Long) {
        outerPart.setProgress(progressMs)
    }
}
