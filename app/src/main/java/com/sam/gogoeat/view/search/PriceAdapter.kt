package com.sam.gogoeat.view.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sam.gogoeat.databinding.ItemPricePopupBinding

class PriceAdapter() : ListAdapter<String, PriceAdapter.ViewHolder>(DiffCallback) {

    private var currentPosition: Int = 0
    private var itemClickListener: ((position: Int)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder(private val binding: ItemPricePopupBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(price: String, position: Int, currentPosition: Int) {
            binding.s = price
            binding.tvPrice.isSelected = position == currentPosition

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemPricePopupBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, parent.context)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val price = getItem(position)

        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(position)
            setCurrentPosition(position)
        }

        holder.bind(price, position, currentPosition)
    }

    fun setCurrentPosition(position: Int) {
        currentPosition = position
    }

    fun currentPosition() = currentPosition

    fun setItemClickListener(itemClickListener: ((position: Int)->Unit)?) {
        this.itemClickListener = itemClickListener
    }
}