package com.github.nukc.recycleradapter;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Nukc.
 */

public abstract class RecyclerAdapter<T> extends BaseAdapter<T> implements ItemProvide{

    private SparseArray<Class<?>> mHolderClasses = new SparseArray<>(0);
    private Object mListener;

    public RecyclerAdapter() {
        super();
    }

    public RecyclerAdapter(List<T> dataList) {
        super(dataList);
    }

    public RecyclerAdapter(Object listener) {
        super();
        mListener = listener;
    }

    public RecyclerAdapter(List<T> dataList, Object listener) {
        super(dataList);
        mListener = listener;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Class<?> holderClass = mHolderClasses.get(viewType);
        View view = inflate(parent, viewType);
        return HolderHelper.newInstance(holderClass, view, mListener);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        holder.onBindView(getItem(position));
    }

    /**
     * @return viewType = the item view of the layout resource id
     */
    @Override
    public int getItemViewType(int position) {
        ItemWrapper itemWrapper = getItemHolder(position);
        if (itemWrapper == null) {
            throw new NullPointerException("ItemWrapper is null, position = " + position);
        }

        int viewType = itemWrapper.getLayoutResId();
        Class<?> holderClass = itemWrapper.getHolderClass();
        addHolderClass(viewType, holderClass);
        return viewType;
    }

    private void addHolderClass(int viewType, Class<?> holderClass) {
        if (mHolderClasses.get(viewType) == null) {
            mHolderClasses.put(viewType, holderClass);
        }
    }

    public void setItemListener(Object listener) {
        mListener = listener;
    }
}
