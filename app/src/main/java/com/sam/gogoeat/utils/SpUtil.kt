package com.sam.gogoeat.utils

import android.content.Context
import com.sam.gogoeat.MyApplication
import com.sam.gogoeat.data.SettingData
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object SpUtil {
    private const val GOGOEAT_SP = "GOGOEAT_SP"
    private val sp = MyApplication.appContext.getSharedPreferences(GOGOEAT_SP, Context.MODE_PRIVATE)

    fun <T> setObject(typeOfObject: Class<T>, key: String, o: T) {
        val adapter: JsonAdapter<T> = Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(typeOfObject)
        MyApplication.appContext
            .getSharedPreferences(GOGOEAT_SP, Context.MODE_PRIVATE)
            .edit().putString(key, adapter.toJson(o))
            .apply()
    }

    fun <T> getObject(typeOfObject: Class<T>, key: String): T? {
        val adapter: JsonAdapter<T> = Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(typeOfObject)
        val json = sp.getString(key, null)
        if (json != null) return adapter.fromJson(json)
        return null
    }
}