package com.joe.donlate.view.base

import androidx.recyclerview.widget.RecyclerView
import java.util.*

abstract class MutableListAdapter<T : Any, R : BaseHolder<T>> : RecyclerView.Adapter<R>() {
    abstract val items: LinkedList<T>

    open fun add(item: T, position: Int = items.size) {
        items.add(position, item)
        notifyItemInserted(position)
    }

    open fun remove(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    open fun add(items: List<T>) {
        val preSize = this.items.size
        this.items.addAll(items)
        notifyItemRangeInserted(preSize, this.items.size)
    }

    fun remove(items: List<T>) {
        this.items.removeAll(items)
        notifyDataSetChanged()
    }

    open fun set(items: List<T>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: R, position: Int) {
        holder.bind(items[position])
    }
}