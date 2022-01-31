package com.sam.gogoeat.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.internal.managers.ViewComponentManager

object NewUtil {

    val FragmentManager.currentNavigationFragment: Fragment?
        get() = primaryNavigationFragment?.childFragmentManager?.fragments?.first()

    // 2個 LiveData 合併
    fun <T, K, R> LiveData<T>.combineWith(
            liveData: LiveData<K>,
            block: (T?, K?) -> R
    ): MediatorLiveData<R> {
        val result = MediatorLiveData<R>()
        result.addSource(this) {
            result.value = block(this.value, liveData.value)
        }
        result.addSource(liveData) {
            result.value = block(this.value, liveData.value)
        }
        return result
    }

    // 3個 LiveData 合併
    fun <T, K, L, R> LiveData<T>.combineWith(
            liveDataA: LiveData<K>,
            liveDataB: LiveData<L>,
            block: (T?, K?, L?) -> R
    ): MediatorLiveData<R> {
        val result = MediatorLiveData<R>()
        result.addSource(this) {
            result.value = block(this.value, liveDataA.value, liveDataB.value)
        }
        result.addSource(liveDataA) {
            result.value = block(this.value, liveDataA.value, liveDataB.value)
        }
        result.addSource(liveDataB) {
            result.value = block(this.value, liveDataA.value, liveDataB.value)
        }
        return result
    }

    // 4個 LiveData 合併
    fun <T, K, L, N, R> LiveData<T>.combineWith(
            liveDataA: LiveData<K>,
            liveDataB: LiveData<L>,
            liveDataC: LiveData<N>,
            block: (T?, K?, L?, N?) -> R
    ): MediatorLiveData<R> {
        val result = MediatorLiveData<R>()
        result.addSource(this) {
            result.value = block(this.value, liveDataA.value, liveDataB.value, liveDataC.value)
        }
        result.addSource(liveDataA) {
            result.value = block(this.value, liveDataA.value, liveDataB.value, liveDataC.value)
        }
        result.addSource(liveDataB) {
            result.value = block(this.value, liveDataA.value, liveDataB.value, liveDataC.value)
        }
        result.addSource(liveDataC) {
            result.value = block(this.value, liveDataA.value, liveDataB.value, liveDataC.value)
        }
        return result
    }

    fun getWrapperContextLifecycleOwner(context: Context): LifecycleOwner {
        val lifecycleOwner: LifecycleOwner
        if (context is ViewComponentManager.FragmentContextWrapper) {
            lifecycleOwner = context.baseContext as LifecycleOwner
        } else {
            lifecycleOwner = context as LifecycleOwner
        }
        return lifecycleOwner
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Context.showKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun isPad(context: Context): Boolean {
        return (context.resources.configuration.screenLayout
                and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    fun <T> Fragment.setBackStackData(key: String, data: T, doBack: Boolean = false) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, data)
        if (doBack) findNavController().popBackStack()
    }

    fun <T> Fragment.setBackStackData(id: Int, key: String, data: T, doBack: Boolean = false): Boolean {
        try {
            findNavController().getBackStackEntry(id).savedStateHandle.set(key, data)
            if (doBack) {
                return findNavController().popBackStack(id, false)
            }
            return true
        } catch (e: Exception) {
            Log.d("sam", "setBackStackData error=${e.localizedMessage}")
        }
        return false
    }

    fun <T> Fragment.getBackStackData(key: String, singleCall : Boolean= true , result: (T) -> (Unit)) {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)
                ?.observe(viewLifecycleOwner) {
                    result(it)
                    //if not removed then when click back without set data it will return previous data
                    if(singleCall) findNavController().currentBackStackEntry?.savedStateHandle?.remove<T>(key)
                }
    }
}