package com.github.nukc.recycleradapter.dsl

import com.github.nukc.recycleradapter.BaseProvider

/**
 * @author Nukc.
 */
abstract class DslProvider<T : Any>(type: Class<*>, val resIds: MutableList<Int>) : BaseProvider<T, ViewHolderDsl<T>>(type)