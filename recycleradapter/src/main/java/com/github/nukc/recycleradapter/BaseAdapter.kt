package com.github.nukc.recycleradapter

import androidx.annotation.MainThread
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter(val items: MutableList<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    @MainThread
    fun refresh(items: MutableList<Any>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    @MainThread
    fun addAll(items: MutableList<Any>) {
        addAll(this.itemCount, items)
    }

    @MainThread
    fun addAll(positionStart: Int, items: MutableList<Any>) {
        this.items.addAll(positionStart, items)
        notifyItemRangeInserted(positionStart, items.size)
    }

    @MainThread
    fun add(position: Int, item: Any) {
        items.add(position, item)
        notifyItemInserted(position)
    }

    @MainThread
    fun add(item: Any) {
        add(items.size, item)
    }

    @MainThread
    fun change(position: Int, item: Any) {
        items[position] = item
        notifyItemChanged(position)
    }

    @MainThread
    fun remove(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    @MainThread
    fun move(fromPosition: Int, toPosition: Int) {
        val item = items.removeAt(fromPosition)
        items.add(toPosition, item)
        notifyItemMoved(fromPosition, toPosition)
    }

    @MainThread
    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }
}