package com.banuba.example.integrationapp.videoeditor.custom.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.banuba.example.integrationapp.R
import com.banuba.sdk.core.ui.ext.dimen

class CustomRecordingButtonOuterView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attr, defStyle) {

    companion object {
        private const val ANGLE_END = 360F
        private const val ANGLE_START = 0F
        private const val INIT_VIEW_ROTATION = -90F
    }

    private var circleWidth = context.dimen(R.dimen.record_button_circle_idle_width)
        set(value) {
            field = value
            gradientPaint.strokeWidth = value
            whitePaint.strokeWidth = value
            isDrawAreaMeasured = false
        }
    private val circleColors = context.resources.getIntArray(R.array.progress_gradient_colors)
    private var circleDrawArea = RectF()

    private var isDrawAreaMeasured = false

    private val gradientPaint by lazy(LazyThreadSafetyMode.NONE) {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = circleWidth
            shader = SweepGradient(
                measuredWidth.toFloat() / 2,
                measuredHeight.toFloat() / 2,
                circleColors,
                null
            )
        }
    }
    private val whitePaint by lazy(LazyThreadSafetyMode.NONE) {
        Paint().apply {
            color = Color.WHITE
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = circleWidth
        }
    }

    private var gradientSweepAngle = 0F

    private val animator = ValueAnimator().apply {
        repeatCount = 0
        setFloatValues(ANGLE_START, ANGLE_END)
        interpolator = LinearInterpolator()
        addUpdateListener {
            gradientSweepAngle = it.animatedValue as Float
            invalidate()
        }
    }

    init {
        // Because 0 degrees angle correspond to 3 o'clock on a watch
        rotation = INIT_VIEW_ROTATION
    }

    override fun onDraw(canvas: Canvas) {
        if (!isDrawAreaMeasured) {
            isDrawAreaMeasured = true
            circleDrawArea = RectF(
                0f + circleWidth,
                0f + circleWidth,
                measuredWidth.toFloat() - circleWidth,
                measuredHeight.toFloat() - circleWidth
            )
        }
        with(canvas) {
            save()
            rotate(gradientSweepAngle, measuredWidth.toFloat() / 2, measuredHeight.toFloat() / 2)
            drawArc(circleDrawArea, ANGLE_START, ANGLE_END, false, gradientPaint)
            restore()
            drawArc(
                circleDrawArea,
                gradientSweepAngle,
                ANGLE_END - gradientSweepAngle,
                false,
                whitePaint
            )
        }
    }

    fun startAnimation(
        availableDurationMs: Long,
        maxDurationMs: Long
    ) {
        circleWidth = context.dimen(R.dimen.record_button_circle_progress_width)
        animator.duration = maxDurationMs
        animator.cancel()
        animator.currentPlayTime = maxDurationMs - availableDurationMs
    }

    fun stopAnimation() {
        circleWidth = context.dimen(R.dimen.record_button_circle_idle_width)
        animator.cancel()
        gradientSweepAngle = 0F
        invalidate()
    }

    fun pauseAnimation() {
        animator.pause()
    }

    fun resumeAnimation() {
        animator.start()
    }
}