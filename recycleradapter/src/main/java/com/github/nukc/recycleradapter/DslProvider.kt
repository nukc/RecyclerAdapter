package com.github.nukc.recycleradapter

import androidx.recyclerview.widget.RecyclerView

/**
 * @author Nukc.
 */
abstract class DslProvider<T : Any, VH : RecyclerView.ViewHolder>(
        type: Class<*>, val resIds: MutableList<Int>) : BaseProvider<T, VH>(type)