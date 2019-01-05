package com.github.nukc.recycleradapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class SimpleProvider<T : Any>(type: Class<*>) : BaseProvider<T, RecyclerView.ViewHolder>(type) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false)) {
            init {
                initHolder(this, this.itemView)
            }
        }
    }

    open fun initHolder(holder: RecyclerView.ViewHolder, itemView: View) {
    }

    override fun bind(holder: RecyclerView.ViewHolder, data: T, payload: List<Any>) {
        bind(holder, data)
    }

    abstract fun bind(holder: RecyclerView.ViewHolder, data: T)

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
    }

    final override fun getLayoutResId(position: Int, data: T): Int {
        return super.getLayoutResId(position, data)
    }
}