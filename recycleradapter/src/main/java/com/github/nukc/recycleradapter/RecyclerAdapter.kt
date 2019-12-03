package com.github.nukc.recycleradapter

import android.util.SparseArray
import android.view.ViewGroup
import androidx.collection.ArrayMap
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(builder: Builder) : BaseAdapter(builder.items) {

    private val providers: SparseArray<BaseProvider<Any, RecyclerView.ViewHolder>> = builder.providers
    private val findCache = ArrayMap<Class<Any>, BaseProvider<Any, RecyclerView.ViewHolder>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return providers[viewType].onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(holder, position, mutableListOf())
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        val provider = providers[holder.itemViewType]
        provider?.bind(holder, items[position], payloads)
    }

    override fun getItemViewType(position: Int): Int {
        val provider = findProviderByPosition(position)
        val resId = provider!!.getLayoutResId(position, items[position])
        if (resId == -1) {
            return provider.getLayoutResId()
        }
        return resId
    }

    override fun getItemId(position: Int): Long {
        return findProviderByPosition(position)!!.getItemId(position)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return providers[holder.itemViewType].onFailedToRecycleView(holder)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        providers[holder.itemViewType].onViewRecycled(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        providers[holder.itemViewType].onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        providers[holder.itemViewType].onViewDetachedFromWindow(holder)
    }

    private fun findProviderByPosition(position: Int): BaseProvider<Any, RecyclerView.ViewHolder>? {
        return findProvider(items[position])
    }

    private fun findProvider(item: Any): BaseProvider<Any, RecyclerView.ViewHolder>? {
        val itemType = getItemType(item)
        var provider: BaseProvider<Any, RecyclerView.ViewHolder>? = findCache[itemType]
        if (provider != null) {
            return provider
        }
        for (i in 0 until providers.size()) {
            val p = providers.valueAt(i)
            if (p.type == itemType || itemType.superclass == p.type) {
                provider = p
                findCache[itemType] = provider
                return provider
            }

            // try to compare item interfaces
            itemType.interfaces.forEach {
                if (it == p.type) {
                    provider = p
                    findCache[itemType] = provider
                    return provider
                }
            }
        }

        throw RuntimeException("Can not find the provider, item<${item.javaClass}>: the runtime Java class of this object" +
                ", is register?, itemType: $itemType")
    }

    companion object {
        /**
         * 为美好的世界送上祝福！
         */
        fun explosion(): Builder {
            return Builder()
        }
    }
}

class Builder {
    val providers: SparseArray<BaseProvider<Any, RecyclerView.ViewHolder>> = SparseArray()
    var items: MutableList<Any> = arrayListOf()
        private set

    @Suppress("UNCHECKED_CAST")
    fun <T : Any, VH : RecyclerView.ViewHolder> register(provider: BaseProvider<T, VH>): Builder {
        when (provider) {
            is MultiTypeProvider -> {
                provider.providerAllLayoutResId().forEach {
                    providers.put(it, provider as BaseProvider<Any, RecyclerView.ViewHolder>)
                }
            }
            is DslProvider -> {
                provider.resIds.forEach {
                    providers.put(it, provider as BaseProvider<Any, RecyclerView.ViewHolder>)
                }
            }
            else -> {
                providers.put(provider.getLayoutResId(), provider as BaseProvider<Any, RecyclerView.ViewHolder>)
            }
        }
        return this
    }

    fun setItems(items: MutableList<Any>): Builder {
        this.items = items
        return this
    }

    fun addItems(items: List<Any>): Builder {
        this.items.addAll(items)
        return this
    }

    fun addItem(item: Any): Builder {
        this.items.add(item)
        return this
    }

    fun build(): RecyclerAdapter {
        return RecyclerAdapter(this)
    }
}