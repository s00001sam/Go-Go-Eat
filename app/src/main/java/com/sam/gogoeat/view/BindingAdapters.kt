package com.sam.gogoeat.view

import android.net.Uri
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sam.gogoeat.R


@BindingAdapter("imageUrl")
fun bindImage(imgView: AppCompatImageView, imgUrl: String?) {
    imgUrl?.let {
        val Uri = Uri.parse(imgUrl)
        val imgUri = Uri.buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    }
}
