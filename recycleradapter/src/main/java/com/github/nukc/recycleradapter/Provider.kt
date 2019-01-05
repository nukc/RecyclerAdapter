package com.github.nukc.recycleradapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

private interface Provider<T, VH : RecyclerView.ViewHolder>   {

    @LayoutRes fun getLayoutResId(): Int

    @LayoutRes fun getLayoutResId(position: Int, data: T): Int = -1

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    fun bind(holder: VH, data: T, payload: List<Any>)

    fun getItemId(position: Int): Long = position.toLong()

    fun onFailedToRecycleView(holder: VH): Boolean = false

    fun onViewRecycled(holder: VH)

    fun onViewAttachedToWindow(holder: VH)

    fun onViewDetachedFromWindow(holder: VH)
}

abstract class BaseProvider<T : Any, VH : RecyclerView.ViewHolder>(val type: Class<*>) : Provider<T, VH>