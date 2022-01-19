package com.sam.gogoeat.view.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.animation.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.sam.gogoeat.databinding.ViewLuckyWheelBinding
import kotlin.random.Random


class LuckyWheelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var w = 0
    private var h = 0
    private var scrollFinish : (()->Unit)? = null

    private var binding = ViewLuckyWheelBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        setOnClickListener { startScroll() }
    }

    fun setScrollFinishListener(finish : (()->Unit)) {
        scrollFinish = finish
    }

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(width, height, oldw, oldh)
        w = width
        h = height
    }

    fun startScroll() {
        val finalDegree = 3600f + Random.nextInt(360).toFloat()
        val rotateAnimation = RotateAnimation(
            0f, finalDegree,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 5000
            fillAfter = true
            interpolator = DecelerateInterpolator(2f)
        }
        rotateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                scrollFinish?.invoke()
            }
        })

        binding.wheelBody.startAnimation(rotateAnimation)
    }

    fun showWithAnimation() {
        val scaleAnimation = ScaleAnimation(
            0f, 1f,
            0f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 1000
            fillAfter = true
            interpolator = BounceInterpolator()
        }
        val rotateAnimation = RotateAnimation(
                0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 300
            fillAfter = true
        }
        val alphaAnimation = AlphaAnimation(0.5f, 1f).apply {
            duration = 1000
            fillAfter = true
        }

        startAnimation(AnimationSet(false).apply {
            addAnimation(scaleAnimation)
            addAnimation(rotateAnimation)
            addAnimation(alphaAnimation)
            fillAfter = true
        })
    }
}