package com.github.nukc.recycleradapter;

import android.support.annotation.LayoutRes;

/**
 * @see ItemProvide#getItemHolder(int)
 * Created by Nukc.
 */

public class ItemWrapper {

    private int mLayoutResId;
    private Class<?> mHolderClass;

    public ItemWrapper(@LayoutRes int layoutResId, Class<?> holderClass) {
        mLayoutResId = layoutResId;
        mHolderClass = holderClass;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

    public Class<?> getHolderClass() {
        return mHolderClass;
    }
}
