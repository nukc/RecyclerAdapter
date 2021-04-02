package com.github.nukc.recycleradapter.dsl

import androidx.viewbinding.ViewBinding
import com.github.nukc.recycleradapter.Builder

/**
 * @author Nukc.
 */
class BuilderDsl(val builder: Builder) {

    var hasStableIds = false

    inline fun <reified T : Any, VB : ViewBinding> renderItem(crossinline block: ProviderDsl<T, VB>.() -> Unit) {
        val providerDsl = ProviderDsl<T, VB>(T::class.java)
        providerDsl.block()
        builder.register(providerDsl.build())
    }
}