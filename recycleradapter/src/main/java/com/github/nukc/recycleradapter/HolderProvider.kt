package com.github.nukc.recycleradapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class HolderProvider<T : Any, VH : RecyclerView.ViewHolder>(type: Class<*>) : BaseProvider<T, VH>(type) {

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return provideHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
    }

    abstract fun provideHolder(itemView: View): VH

    override fun bind(holder: VH, data: T, payload: List<Any>) {
        bind(holder, data)
    }

    abstract fun bind(holder: VH, data: T)

    override fun onViewRecycled(holder: VH) {
    }

    override fun onViewAttachedToWindow(holder: VH) {
    }

    override fun onViewDetachedFromWindow(holder: VH) {
    }

    final override fun getLayoutResId(position: Int, data: T): Int {
        return super.getLayoutResId(position, data)
    }
}