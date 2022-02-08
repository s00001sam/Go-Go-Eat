package com.sam.gogoeat.utils

import android.animation.*
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources.getSystem
import android.graphics.Paint
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.model.LatLng
import com.sam.gogoeat.MyApplication
import com.sam.gogoeat.data.place.PlaceData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


object Util {

    fun Activity.gotoMap(store: PlaceData) {
        val gmmIntentUri: Uri =
            Uri.parse("https://www.google.com/maps/search/?api=1&query=qwerty&query_place_id=${store.place_id}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    fun checkHasPermission(permission: String) : Boolean {
        return ActivityCompat.checkSelfPermission(MyApplication.appContext, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun getRandomNum(totalNum: Int) : Int {
        return (0 until totalNum).random()
    }

    fun LatLng.getDinstance(end: LatLng): Int{
        val lat1: Double = (Math.PI / 180)*(this.latitude)
        val lat2: Double = (Math.PI / 180)*(end.latitude)
        val lon1: Double = (Math.PI / 180)*(this.longitude)
        val lon2: Double = (Math.PI / 180)*(end.longitude)
        val r: Double = 6371.0
        val d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1))*r

        return (d*1000).toInt()
    }

    //搖晃動畫
    fun View.startShakeAnim(scaleSmall: Float, scaleLarge: Float, shakeDegrees: Float, duration: Long, endListener: () -> Unit) {
        //先變小再變大
        val scaleXValuesHolder: PropertyValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        )
        val scaleYValuesHolder: PropertyValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        )

        //先往左再往右
        val rotateValuesHolder: PropertyValuesHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, -shakeDegrees),
                Keyframe.ofFloat(0.2f, shakeDegrees),
                Keyframe.ofFloat(0.3f, -shakeDegrees),
                Keyframe.ofFloat(0.4f, shakeDegrees),
                Keyframe.ofFloat(0.5f, -shakeDegrees),
                Keyframe.ofFloat(0.6f, shakeDegrees),
                Keyframe.ofFloat(0.7f, -shakeDegrees),
                Keyframe.ofFloat(0.8f, shakeDegrees),
                Keyframe.ofFloat(0.9f, -shakeDegrees),
                Keyframe.ofFloat(1.0f, 0f)
        )
        val objectAnimator: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, scaleXValuesHolder, scaleYValuesHolder, rotateValuesHolder)
        objectAnimator.duration = duration
        objectAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                endListener.invoke()
            }
        })
        objectAnimator.start()
    }

    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun Paint.baseApply() {
        isAntiAlias = true //抗鋸齒
        strokeWidth = 20f
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    fun String.toast() {
        Toast.makeText(MyApplication.appContext, this, Toast.LENGTH_SHORT).show()
    }

    fun <T> Flow<T>.collectFlow(lifecycleOwner: LifecycleOwner, doSomething:((t: T)-> Unit)) {
        this.onEach {
            doSomething.invoke(it)
        }.launchWhenStarted(lifecycleOwner)
    }

    private fun <T> Flow<T>.launchWhenStarted(lifecycleOwner: LifecycleOwner)= with(lifecycleOwner) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                try {
                    this@launchWhenStarted.collect()
                }catch (t: Throwable){
                    Logger.d("launchWhenStarted throwable=${t.localizedMessage}")
                }
            }
        }
    }

    fun Int.px2dp() = (this / getSystem().displayMetrics.density).toInt()

    fun Int.dp2px() = (this * getSystem().displayMetrics.density).toInt()

    fun View.showKeyboard() {
        (MyApplication.appContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(this, InputMethodManager.SHOW_FORCED)
    }

    fun View.hideKeyboard() {
        (MyApplication.appContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(windowToken, 0)
    }
}