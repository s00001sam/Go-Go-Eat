package com.sam.gogoeat.utils

import android.animation.*
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.model.LatLng
import com.sam.gogoeat.data.place.PlaceData
import kotlin.random.Random


object Util {

    fun Activity.gotoMap(store: PlaceData) {
        val gmmIntentUri: Uri =
            Uri.parse("https://www.google.com/maps/search/?api=1&query=qwerty&query_place_id=${store.place_id}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    fun checkHasPermission(context: Context, permission: String) : Boolean {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
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
        val d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1)* Math.cos(lat2)* Math.cos(lon2-lon1))*r

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
        objectAnimator.addListener(object: AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                endListener.invoke()
            }
        })
        objectAnimator.start()
    }
}