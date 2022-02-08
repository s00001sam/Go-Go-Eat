package com.sam.gogoeat.utils

import android.view.View
import androidx.databinding.BindingAdapter


@BindingAdapter("isSelected")
fun bindImage(view: View, isSelected: Boolean) {
    view.isSelected = isSelected
}