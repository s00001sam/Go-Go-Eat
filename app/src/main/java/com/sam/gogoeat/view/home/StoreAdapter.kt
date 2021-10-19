package com.sam.gogoeat.view.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sam.gogoeat.R
import com.sam.gogoeat.data.place.PlaceData
import com.sam.gogoeat.databinding.ItemStoreRcyBinding
import com.sam.gogoeat.utils.UserManager
import com.sam.gogoeat.utils.Util.getDinstance

class StoreAdapter(private val onClickListener: OnclickListener) : ListAdapter<PlaceData, StoreAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder(private val binding: ItemStoreRcyBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(store: PlaceData) {
            binding.store = store

            if (store.rating != 0.0 && store.user_ratings_total != 0) {
                binding.textStars.visibility = View.VISIBLE
                binding.textStars.text =  "${store.rating} (${store.user_ratings_total})"
            } else {
                binding.textStars.visibility = View.GONE
            }

            if (store.opening_hours?.open_now != null) {
                binding.textOpen.visibility = View.VISIBLE
                binding.textOpen.text = context.getString(if (store.opening_hours.open_now) R.string.text_is_open else R.string.text_is_not_open)
            } else {
                binding.textOpen.visibility = View.GONE
            }

            "${store.distance} m".also { binding.textMeter.text = it }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemStoreRcyBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, parent.context)
            }
        }
    }


    companion object DiffCallback : DiffUtil.ItemCallback<PlaceData>() {
        override fun areItemsTheSame(oldItem: PlaceData, newItem: PlaceData): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PlaceData, newItem: PlaceData): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val store = getItem(position)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(store)
        }

        holder.bind(store)
    }

    class OnclickListener(val clickListener: (store: PlaceData) -> Unit){
        fun onClick (store: PlaceData) = clickListener(store)
    }
}