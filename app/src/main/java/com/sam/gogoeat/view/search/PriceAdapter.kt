package com.sam.gogoeat.view.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sam.gogoeat.databinding.ItemPricePopupBinding
import com.sam.gogoeat.view.support.BaseBindingAdapter

class PriceAdapter : BaseBindingAdapter<String, ItemPricePopupBinding>() {

    private var currentPosition: Int = 0
    private var itemClickListener: ((position: Int)->Unit)? = null

    override fun bindView(binding: ItemPricePopupBinding, position: Int, data: String) {
        binding.s = data
        binding.tvPrice.isSelected = position == currentPosition
        binding.root.setOnClickListener {
            itemClickListener?.invoke(position)
            setCurrentPosition(position)
        }
    }

    override fun createBinding(context: Context, parent: ViewGroup): ItemPricePopupBinding {
        return ItemPricePopupBinding.inflate(LayoutInflater.from(context), parent, false)
    }

    fun setCurrentPosition(position: Int) {
        currentPosition = position
    }

    fun currentPosition() = currentPosition

    fun setItemClickListener(itemClickListener: ((position: Int)->Unit)?) {
        this.itemClickListener = itemClickListener
    }
}