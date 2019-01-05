package com.github.nukc.recycleradapter

import androidx.recyclerview.widget.RecyclerView

abstract class PureLayoutProvider<T : Any>(type: Class<*>) : SimpleProvider<T>(type) {

    override fun bind(holder: RecyclerView.ViewHolder, data: T) {
    }

}