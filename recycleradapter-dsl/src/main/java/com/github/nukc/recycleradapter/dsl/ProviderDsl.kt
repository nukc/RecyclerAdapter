package com.github.nukc.recycleradapter.dsl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.github.nukc.recycleradapter.DslProvider

class ProviderDsl<T : Any>(var type: Class<*>) {
    private var resIds: MutableList<Int> = mutableListOf()
    private var resSetup: (ViewHolderDsl<T>.() -> Unit)? = null
    private var bind: ViewHolderDsl<T>.() -> Unit = {}
    private var onRecycled: ViewHolderDsl<T>.() -> Unit = {}
    private var onAttached: ViewHolderDsl<T>.() -> Unit = {}
    private var onDetached: ViewHolderDsl<T>.() -> Unit = {}
    private var onFailed: ViewHolderDsl<T>.() -> Boolean = { false }
    private var getItemId: (position: Int) -> Long = { it.toLong() }
    private var getItemViewType: (ItemViewTypeDsl<T>.() -> Int)? = null

    /**
     * 设置 Item 的布局
     * @param id item 的布局 id
     * @param setup  可在这里对 view 进行设置，可空; [ProviderDsl.build] [DslProvider.onCreateViewHolder]
     */
    fun res(@LayoutRes id: Int, setup: (ViewHolderDsl<T>.() -> Unit)? = null) {
        resIds.add(id)
        resSetup = setup
    }

    /**
     * 设置 Item 的多个布局
     * @param ids item 的布局 id，多个
     * @param setup  可在这里对 view 进行设置，可空; [ProviderDsl.build] [DslProvider.onCreateViewHolder]
     */
    fun res(@LayoutRes vararg ids: Int, setup: (ViewHolderDsl<T>.() -> Unit)? = null) {
        resIds.addAll(ids.toList())
        resSetup = setup
    }

    /**
     * 同 [androidx.recyclerview.widget.RecyclerView.Adapter.onBindViewHolder]
     * @param block 可更新 [androidx.recyclerview.widget.RecyclerView.ViewHolder.itemView] 里的视图，
     * @            方法块内 this 是 [ViewHolderDsl]，可拿到 data
     * @            继承 [androidx.recyclerview.widget.RecyclerView.ViewHolder]，[kotlinx.android.extensions.LayoutContainer]
     */
    fun bind(block: ViewHolderDsl<T>.() -> Unit) {
        bind = block
    }

    /**
     * 同 [androidx.recyclerview.widget.RecyclerView.Adapter.onViewRecycled]
     */
    fun onRecycled(block: ViewHolderDsl<T>.() -> Unit) {
        onRecycled = block
    }

    /**
     * 同 [androidx.recyclerview.widget.RecyclerView.Adapter.onAttachedToRecyclerView]
     */
    fun onAttached(block: ViewHolderDsl<T>.() -> Unit) {
        onAttached = block
    }

    /**
     * 同 [androidx.recyclerview.widget.RecyclerView.Adapter.onDetachedFromRecyclerView]
     */
    fun onDetached(block: ViewHolderDsl<T>.() -> Unit) {
        onDetached = block
    }

    /**
     * 同 [androidx.recyclerview.widget.RecyclerView.Adapter.getItemId]
     */
    fun getItemId(block: (position: Int) -> Long) {
        getItemId = block
    }

    /**
     * 同 [androidx.recyclerview.widget.RecyclerView.Adapter.getItemViewType]
     */
    fun getItemViewType(block: ItemViewTypeDsl<T>.() -> Int) {
        getItemViewType = block
    }

    fun build(): DslProvider<T, ViewHolderDsl<T>> {

        return object : DslProvider<T, ViewHolderDsl<T>>(type, resIds) {

            override fun getLayoutResId() = resIds.first()

            override fun getLayoutResId(position: Int, data: T): Int {
                return getItemViewType?.let { ItemViewTypeDsl(position, data).it() } ?: -1
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                    : ViewHolderDsl<T> {
                val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
                val vhDsl = ViewHolderDsl<T>(view)
                resSetup?.let { vhDsl.it() }
                return vhDsl
            }

            override fun bind(holder: ViewHolderDsl<T>, data: T, payloads: List<Any>) {
                holder.data = data
                holder.bind()
            }

            override fun onViewRecycled(holder: ViewHolderDsl<T>) = holder.onRecycled()

            override fun onViewAttachedToWindow(holder: ViewHolderDsl<T>) =
                    holder.onAttached()

            override fun onViewDetachedFromWindow(holder: ViewHolderDsl<T>) =
                    holder.onDetached()

            override fun onFailedToRecycleView(holder: ViewHolderDsl<T>) =
                    holder.onFailed()

            override fun getItemId(position: Int) = this@ProviderDsl.getItemId(position)
        }

    }
}