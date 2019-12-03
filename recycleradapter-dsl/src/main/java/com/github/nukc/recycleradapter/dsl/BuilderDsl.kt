package com.github.nukc.recycleradapter.dsl

import com.github.nukc.recycleradapter.Builder

/**
 * @author Nukc.
 */
class BuilderDsl(val builder: Builder) {

    var hasStableIds = false

    inline fun <reified T : Any> renderItem(crossinline block: ProviderDsl<T>.() -> Unit) {
        val providerDsl = ProviderDsl<T>(T::class.java)
        providerDsl.block()
        builder.register(providerDsl.build())
    }
}