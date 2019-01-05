package com.github.nukc.recycleradapter

@Suppress("UNCHECKED_CAST")
fun <T : Any> getItemType(item: T): Class<T> {
    if (item is Collection<*>) {
        for (v in item.iterator()) {
            return v?.javaClass as Class<T>
        }
    }
    return item.javaClass
}

//typealias Binder<T> = (RecyclerView.ViewHolder, T) -> Unit

//typealias Items = MutableList<Any>