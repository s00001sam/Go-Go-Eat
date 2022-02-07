package com.sam.gogoeat.view.customview

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.sam.gogoeat.R
import com.sam.gogoeat.utils.Logger
import com.sam.gogoeat.utils.Util.baseApply

class StartLogoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var animationEnd: (()->Unit)? = null

    private var r = 1.0f
    private var w = 0
    private var h = 0
    private val path: Path = Path()
    private val finalPath: Path = Path()
    private var paint: Paint? = null
    private var mLength = 0f
    private var mAnimValue = 0f
    private var mEffect: PathEffect? = null
    private val colors = intArrayOf(
        ContextCompat.getColor(context, R.color.red),
        ContextCompat.getColor(context, R.color.orange),
        ContextCompat.getColor(context, R.color.light_yellow))

    private var positions = floatArrayOf(0.0f, 0.5f, 1.0f)

    private val mPathMeasure: PathMeasure by lazy { PathMeasure() }

    private var isFinal = false

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h
        initWedgits()
        r = w / 2000f
        startGogoPath()
        Logger.d("sam00 StartLogoView w=$w h=$h r=$r")
    }

    private fun initWedgits() {
        paint = Paint()
        paint?.baseApply()
        paint?.strokeWidth = 12f
        val linearGradient = LinearGradient(
            0f, 0f,
            w.toFloat(), h.toFloat(), colors, positions, Shader.TileMode.CLAMP
        )
        paint?.shader = linearGradient
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint?.let { canvas?.drawPath(path, it) }
        paint?.let { canvas?.drawPath(finalPath, it) }
    }

    fun startGogoPath() {
        //g
        path.arcTo(RectF(100f * r, 295f * r, 262f * r, 504f * r), -30f, -280f)
        path.lineTo(220f * r, 627f * r)
        path.arcTo(RectF(110f * r, 500f * r, 222f * r, 700f * r), 18f, 120f)
        path.quadTo(118f * r, 642f * r, 127f * r, 615f * r)
        //o
        path.arcTo(RectF(350f * r, 295f * r, 530f * r, 535f * r), -75f, -315f)
        path.arcTo(RectF(474f * r, 345f * r, 525f * r, 390f * r), -60f, -250f)
        //g
        path.arcTo(RectF(636f * r, 295f * r, 800f * r, 504f * r), -70f, -240f)
        path.lineTo(757f * r, 627f * r)
        path.arcTo(RectF(646f * r, 500f * r, 758f * r, 700f * r), 18f, 120f)
        path.quadTo(654f * r, 642f * r, 663f * r, 615f * r)
        //o
        path.arcTo(RectF(886f * r, 295f * r, 1066f * r, 535f * r), -75f, -315f)

       //e
        path.arcTo(RectF(1244f * r, 136f * r, 1428f * r, 395f * r), -30f, -270f)
        path.arcTo(RectF(1200f * r, 360f * r, 1450f * r, 656f * r), -65f, -250f)
        //a
        path.quadTo(1525f * r, 452f * r, 1581f * r, 290f * r)
        path.arcTo(RectF(1500f * r, 287f * r, 1666f * r, 533f * r), -60f, -315f)
        path.lineTo(1667f * r, 562f * r)
        //t
        path.quadTo(1820f * r, 428f * r, 1829f * r, 100f * r)
        path.quadTo(1725f * r, 420f * r, 1853f * r, 580f * r)
        mPathMeasure.setPath(path, false)
        mLength = mPathMeasure.length
        startAnimation(3000)
    }

    fun startfinalPath() {
        isFinal = true
        finalPath.moveTo(1725f * r,272f * r)
        finalPath.lineTo(1878f * r,272f * r)
        mPathMeasure.setPath(finalPath, false)
        mLength = mPathMeasure.length
        startAnimation(100)
    }

    private fun startAnimation(duration: Long) {
        val animator = ValueAnimator.ofFloat(1f, 0f);
        animator.duration = duration
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { valueAnimator ->
            mAnimValue = valueAnimator.animatedValue as Float
            mEffect = DashPathEffect(floatArrayOf(mLength, mLength), mLength * mAnimValue)
            paint?.pathEffect = mEffect
            invalidate()
        }
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                if (isFinal) animationEnd?.invoke()
                else startfinalPath()
            }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
        })
        animator.start()
    }

    fun setAnimationEndListener(animationEnd: (()->Unit)) {
        this.animationEnd = animationEnd
    }

}