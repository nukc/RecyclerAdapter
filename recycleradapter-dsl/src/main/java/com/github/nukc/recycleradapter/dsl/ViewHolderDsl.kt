package com.github.nukc.recycleradapter.dsl

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @author Nukc.
 */
class ViewHolderDsl<T : Any, VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root) {
    lateinit var data: T
}