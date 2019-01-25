package com.joe.donlate.view

import androidx.recyclerview.widget.RecyclerView

abstract class MutableListAdapter<T, R : BaseHolder<T>> : RecyclerView.Adapter<R>() {
    abstract val items: MutableList<T>

    fun add(item: T, position: Int? = items.size + 1) {
        position?.let {
            items.add(it, item)
            notifyItemInserted(it)
        } ?: notifyAddItem(item)
    }

    private fun notifyAddItem(item: T) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun remove(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun add(items: List<T>) {
        //Todo : 필요시 기능추가
    }

    fun remove(items: List<T>) {
        //Todo : 필요시 기능추가
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: R, position: Int) {
        holder.bind(items[position])
    }
}