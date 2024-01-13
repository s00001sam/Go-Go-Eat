package com.sam.gogoeat.view.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.sam.gogoeat.R
import com.sam.gogoeat.utils.Util.baseApply

class LuckyWheelBodyView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var w = 0
    private var h = 0
    private var trianglePath: Path = Path()
    private var paint: Paint? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h
        initWidget()
    }

    private fun initWidget() {
        // background
        paint = Paint()
        paint?.baseApply()
        paint?.style = Paint.Style.FILL
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBody(canvas)
        drawOutSide(canvas)
    }

    private fun drawBody(canvas: Canvas?) {
        paint?.style = Paint.Style.FILL
        val colors = listOf(
                R.color.yellow_top, R.color.orange_top, R.color.pink_top,
                R.color.green_top, R.color.blue_top
        )
        paint?.let {
            var angle = -90f
            colors.forEach { color ->
                it.color = ContextCompat.getColor(context, color)
                canvas?.drawArc(
                        RectF(20f, 20f, w - 20f, w - 20f),
                        angle, 72f, true, it
                )
                angle += 72f
            }
        }
    }

    private fun drawOutSide(canvas: Canvas?) {
        paint?.let {
            it.strokeWidth = 20f
            it.color = ContextCompat.getColor(context, R.color.yellow_bg)
            it.style = Paint.Style.STROKE
            canvas?.drawCircle(w/2f, w/2f, w/2f - 20f, it)
        }
    }

    private fun drawTopTriangle(canvas: Canvas?) {
        trianglePath.moveTo(w/2f - 20f, 10f)
        trianglePath.lineTo(w/2f + 20f, 10f)
        trianglePath.lineTo(w/2f, 60f)
        trianglePath.lineTo(w/2f - 20f, 10f)
    }

}
