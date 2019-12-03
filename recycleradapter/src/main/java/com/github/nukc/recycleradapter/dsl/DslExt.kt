package com.github.nukc.recycleradapter.dsl

import androidx.recyclerview.widget.RecyclerView
import com.github.nukc.recycleradapter.Builder
import com.github.nukc.recycleradapter.RecyclerAdapter

/**
 * @author Nukc.
 */
/**
 * 设置 LayoutManager 和 Adapter
 * @param lm [RecyclerView.LayoutManager]
 * @param block [BuilderDsl]
 * @return [RecyclerAdapter]
 */
fun RecyclerView.setup(
        lm: RecyclerView.LayoutManager,
        block: BuilderDsl.() -> Unit
): RecyclerAdapter {
    layoutManager = lm
    val builder = RecyclerAdapter.explosion()
    val builderDsl = builder.dsl()
    builderDsl.block()
    val ra = builder.build()
    ra.setHasStableIds(builderDsl.hasStableIds)
    adapter = ra
    return ra
}

fun Builder.dsl() = BuilderDsl(this)