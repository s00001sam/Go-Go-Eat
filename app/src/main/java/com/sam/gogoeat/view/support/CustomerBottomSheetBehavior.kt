package com.sam.gogoeat.view.support

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sam.gogoeat.MyApplication
import com.sam.gogoeat.utils.Util

class CustomerBottomSheetBehavior<V: View> : BottomSheetBehavior<V> {
    constructor() : super()

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {

        if ((event.y - child.y) > Util.dip2px(MyApplication.appContext, 50f)) return false

        return super.onInterceptTouchEvent(parent, child, event)
    }
}