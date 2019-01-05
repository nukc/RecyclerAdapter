package com.github.nukc.recycleradapter.sample

import android.content.Context

class Utils {
    companion object {
        fun dip2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }
    }
}