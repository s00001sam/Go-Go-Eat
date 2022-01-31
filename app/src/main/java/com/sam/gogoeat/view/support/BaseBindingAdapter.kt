package com.sam.gogoeat.view.support

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

//abstract class BaseBindingAdapter<D: Any, B: ViewDataBinding>() :
//        ListAdapter<D, BaseBindingAdapter.ViewHolder<B>>(BaseDiffCallback()) {
//
//    abstract fun createBinding(context: Context, parent: ViewGroup): B
//    abstract fun bindView(binding: B, position: Int, data: D)
//
//    class ViewHolder<B : ViewDataBinding>(val binding: B) : RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<B> {
//        return ViewHolder(createBinding(parent.context, parent))
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder<B>, position: Int) {
//        val item = getItem(position)
//        bindView(holder.binding, position, item)
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun setData(list: List<D>) {
//        submitList(list)
//        notifyDataSetChanged()
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun insertMoreData(newList: List<D>) {
//        val list = currentList as MutableList<D>
//        list.addAll(newList)
//        submitList(list)
//        notifyDataSetChanged()
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun clearData() {
//        submitList(null)
//        notifyDataSetChanged()
//    }
//
//}