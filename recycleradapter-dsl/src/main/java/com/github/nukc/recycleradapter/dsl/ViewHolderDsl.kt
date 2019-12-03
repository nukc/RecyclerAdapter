package com.github.nukc.recycleradapter.dsl

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

/**
 * @author Nukc.
 */
class ViewHolderDsl<T : Any>(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {
    lateinit var data: T
}