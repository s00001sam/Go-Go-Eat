package com.sam.gogoeat.view.support

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.widget.PopupWindowCompat
import androidx.fragment.app.Fragment
import com.sam.gogoeat.databinding.LayoutPricePopupBinding
import com.sam.gogoeat.utils.UserManager
import com.sam.gogoeat.view.search.PriceAdapter


class PriceSpinner(private val fragment: Fragment) {

    private var popupWindow: PopupWindow? = null
    private val priceAdapter : PriceAdapter by lazy { PriceAdapter() }
    private var popBinding: LayoutPricePopupBinding? = null
    private var dismissListener: (()->Unit)? = null
    private var showListener: (()->Unit)? = null

    init {
        initPopupWindow()
    }

    private fun initPopupWindow() {
        popBinding = LayoutPricePopupBinding.inflate(LayoutInflater.from(fragment.requireContext()), null, false)
        popBinding?.root?.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        popBinding?.cardRvPrice?.clipToOutline = true
        popBinding?.rvPrice?.adapter = priceAdapter

        popupWindow = PopupWindow(popBinding?.root)
        popupWindow?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        popupWindow?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        popupWindow?.isOutsideTouchable = true
        popupWindow?.isTouchable = true
        popupWindow?.isFocusable = true

        popupWindow?.setOnDismissListener {
            dismissListener?.invoke()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun show(view: View, width: Int, position: Int) {
        popupWindow?.width = width
        priceAdapter.setCurrentPosition(position)
        priceAdapter.submitList(UserManager.PRICE_STR_LIST)
        priceAdapter.notifyDataSetChanged()
        showListener?.invoke()
        Handler(Looper.getMainLooper()).postDelayed({ //延遲一點等鍵盤下去
            popupWindow?.let { window ->
                popBinding?.rvPrice?.smoothScrollToPosition(priceAdapter.currentPosition())
                PopupWindowCompat.showAsDropDown(
                    window, view, 0, 0, Gravity.CENTER_HORIZONTAL
                )
            }
        }, 50)
    }

    fun dismiss() {
        popupWindow?.dismiss()
    }

    fun setDismissListener(dismissListener: (()->Unit)) {
        this.dismissListener = dismissListener
    }

    fun setShowListener(showListener: (()->Unit)) {
        this.showListener = showListener
    }

    fun setItemClickListener(itemClickListener: ((priceLevel: Int, priceStr: String)->Unit)) {
        priceAdapter.setItemClickListener {
            itemClickListener.invoke(it, UserManager.PRICE_STR_LIST[it])
            dismiss()
        }
    }
}