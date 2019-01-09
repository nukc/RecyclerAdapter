package com.github.nukc.recycleradapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class MultiTypeProvider<T : Any, VH : RecyclerView.ViewHolder>(type: Class<*>) : BaseProvider<T, VH>(type) {

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return provideHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false), viewType)
    }

    abstract fun provideHolder(itemView: View, viewType: Int): VH

    override fun bind(holder: VH, data: T, payloads: List<Any>) {
        bind(holder, data)
    }

    abstract fun bind(holder: VH, data: T)

    override fun onViewRecycled(holder: VH) {
    }

    override fun onViewAttachedToWindow(holder: VH) {
    }

    override fun onViewDetachedFromWindow(holder: VH) {
    }

    /**
     * provider all view type of items
     * 需要先提供全部类型的 layoutResId
     * @see Builder.register
     * @return IntArray 数组形式
     */
    abstract fun providerAllLayoutResId(): IntArray

    final override fun getLayoutResId(): Int = -1

    abstract override fun getLayoutResId(position: Int, data: T): Int
}