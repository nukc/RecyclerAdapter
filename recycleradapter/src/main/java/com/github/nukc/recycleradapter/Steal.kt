package com.github.nukc.recycleradapter

@Suppress("UNCHECKED_CAST")
fun <T : Any> getItemType(item: T): Class<T> {
    if (item is Collection<*>) {
        for (v in item.iterator()) {
            if (v != null) {
                return getItemType(v as T)
            }
        }
    }
    return item.javaClass
}