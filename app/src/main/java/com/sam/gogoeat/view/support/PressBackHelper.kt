package com.sam.gogoeat.view.support

import android.app.Activity
import com.sam.gogoeat.R
import com.sam.gogoeat.utils.Logger
import com.sam.gogoeat.utils.Util.toast

class PressBackHelper {
    private var preTime: Long = 0

    fun back(activity: Activity) {
        val now = System.currentTimeMillis()
        if ((now - preTime) > 3000L) {
            activity.getString(R.string.click_again_back).toast()
            preTime = now
        } else {
            activity.finish()
        }
    }
}