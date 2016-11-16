package com.github.nukc.recycleradapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by C on 11/10/2016.
 */

public abstract class RecyclerHolder<T> extends RecyclerView.ViewHolder {

    public RecyclerHolder(View itemView) {
        super(itemView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @see RecyclerAdapter#onBindViewHolder(RecyclerView.ViewHolder, int)
     * @param t itemData
     */
    public abstract void onBindView(T t);

}
