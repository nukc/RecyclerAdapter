package com.github.nukc.recycleradapter.sample

import android.view.View
import androidx.viewpager.widget.ViewPager

private const val SCALE_MAX = 0.8f

class AlphaAndScalePageTransformer : ViewPager.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        val scale = if (position < 0) {
            (1 - SCALE_MAX) * position + 1
        } else {
            (SCALE_MAX - 1) * position + 1
        }
        if (position < 0) {
            page.pivotX = page.width.toFloat()
            page.pivotY = (page.height / 2).toFloat()
        } else {
            page.pivotX = 0f
            page.pivotY = (page.height / 2).toFloat()
        }
        page.scaleX = scale
        page.scaleY = scale
    }

}