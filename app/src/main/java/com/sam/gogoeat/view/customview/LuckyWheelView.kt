package com.sam.gogoeat.view.customview

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.*
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.os.postDelayed
import androidx.core.view.marginTop
import com.sam.gogoeat.R
import com.sam.gogoeat.data.GogoPlace
import com.sam.gogoeat.databinding.ViewLuckyWheelBinding
import com.sam.gogoeat.utils.Logger
import com.sam.gogoeat.utils.Util
import kotlin.random.Random


class LuckyWheelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var w = 0
    private var h = 0
    private var scrollFinish : (()->Unit)? = null
    private var storeList = listOf<GogoPlace>()
    private var randomIndex: Int = 0

    private var binding = ViewLuckyWheelBinding.inflate(LayoutInflater.from(context), this, true)

    private fun initField() {
        storeList = listOf()
        randomIndex = 0
    }

    fun setScrollFinishListener(finish : (()->Unit)) {
        scrollFinish = finish
    }

    fun setList(list: List<GogoPlace>?) {
        storeList = list ?: listOf()
    }

    fun setRandomIndex(index: Int) {
        randomIndex = index
    }


    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(width, height, oldw, oldh)
        if (w == width && h == height) return
        w = width
        h = height
        initTextSwitcher()
        initField()
    }

    @SuppressLint("Recycle")
    fun startScroll() {
        var count = 1
        val finalDegree = 3600f + Random.nextInt(360).toFloat()
        val animator =
            ObjectAnimator.ofFloat(binding.wheelBody, "rotation", 0f, finalDegree)
                .apply {
                    duration = 5000
                    interpolator = DecelerateInterpolator(2f)
                }
        animator.addUpdateListener {
            val currentRotation = it.getAnimatedValue("rotation") as? Float
            Logger.d("sam00 wheel view rotate=$currentRotation")
            currentRotation?.let { angle ->
                val switchPoint = 75f * (count)
                if (finalDegree - angle <= 30f) {
                    setTextSwitch(count, true)
                    count = 1
                    return@let
                }
                if (angle > switchPoint) {
                    setTextSwitch(count)
                    count++
                }
            }
        }
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                scrollFinish?.invoke()
            }
        })

        animator.start()
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

    private fun initTextSwitcher() {
        binding.tvDefault.visibility = View.VISIBLE
        binding.tsLuckyWheel.setFactory {
            val textView = TextView(this.context).apply {
                gravity = Gravity.CENTER
                textSize = 20f
                setTextColor(ContextCompat.getColor(context, R.color.black_4F4F4F))
                maxLines = 2
                ellipsize = TextUtils.TruncateAt.END
            }
            textView
        }
    }

    private fun setTextSwitch(count: Int, isFinal: Boolean = false) {
        if (storeList.isEmpty()) return
        binding.tvDefault.visibility = View.GONE
        val nowIndex = count % storeList.size
        binding.tsLuckyWheel.setText(
            if (!isFinal) storeList[nowIndex].name else storeList[randomIndex].name
        )
    }
}