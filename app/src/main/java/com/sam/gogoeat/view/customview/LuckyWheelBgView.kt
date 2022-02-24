package com.sam.gogoeat.view.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.sam.gogoeat.R
import com.sam.gogoeat.utils.Util.baseApply

class LuckyWheelBgView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var w = 0
    private var h = 0
    private var trianglePath: Path = Path()
    private var outSidePath: Path = Path()
    private var paint: Paint? = null
    private val mPathMeasure: PathMeasure by lazy { PathMeasure() }
    private var mLength = 0f


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h
        initWidget()
        setOutSidePath()
        setTopTrianglePath()
    }

    private fun initWidget() {
        paint = Paint()
        paint?.baseApply()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawOutSide(canvas)
        drawCenter(canvas)
        drawTopTriangle(canvas)
        drawOutSidePoint(canvas)
    }

    private fun drawCenter(canvas: Canvas?) {
        paint?.let {
            it.style = Paint.Style.FILL
            it.color = ContextCompat.getColor(context, R.color.yellow_bg)
            canvas?.drawCircle(w/2f, w/2f, w/4f, it)
        }
    }

    private fun setOutSidePath() {
        outSidePath.addArc(RectF(20f, 20f, w - 20f, w - 20f ), -90f, 360f)
        mPathMeasure.setPath(outSidePath, false)
        mLength = mPathMeasure.length
    }

    private fun drawOutSide(canvas: Canvas?) {
        paint?.let {
            it.style = Paint.Style.STROKE
            it.color = ContextCompat.getColor(context, R.color.yellow_bg)
            it.strokeWidth = 20f
            canvas?.drawPath(outSidePath, it)
        }
    }

    private fun drawOutSidePoint(canvas: Canvas?) {
        val pos = FloatArray(2)
        val tan = FloatArray(2)
        paint?.let {
            it.style = Paint.Style.STROKE
            it.color = ContextCompat.getColor(context, R.color.black_4F4F4F)
            it.strokeWidth = 1f
            for (i in 0..4) {
                mPathMeasure.getPosTan(mLength * i / 5f, pos , tan)
                canvas?.drawCircle(pos[0], pos[1], 3f, it)
            }
        }
    }

    private fun setTopTrianglePath() {
        trianglePath.moveTo(w/2f - 20f, 10f)
        trianglePath.lineTo(w/2f + 20f, 10f)
        trianglePath.lineTo(w/2f, 60f)
        trianglePath.lineTo(w/2f - 20f, 10f)
    }

    private fun drawTopTriangle(canvas: Canvas?) {
        paint?.let {
            it.style = Paint.Style.FILL
            it.color = ContextCompat.getColor(context, R.color.black_4F4F4F)
            it.strokeWidth = 5f
            canvas?.drawPath(trianglePath, it)
        }
    }

}