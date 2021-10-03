package com.sam.gogoeat.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import com.sam.gogoeat.data.place.PlaceData


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
}