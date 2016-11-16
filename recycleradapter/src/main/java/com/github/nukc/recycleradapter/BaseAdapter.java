package com.github.nukc.recycleradapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nukc.
 */

abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerHolder> {

    protected List<T> mDataList;

    public BaseAdapter() {
        mDataList = new ArrayList<>(0);
    }

    public BaseAdapter(List<T> dataList) {
        mDataList = dataList;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public View inflate(ViewGroup parent, @LayoutRes int resource) {
        return LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
    }

    public T getItem(int position) {
        return mDataList.get(position);
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public void refresh(List<T> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void add(int position, T date) {
        mDataList.add(position, date);
        notifyItemInserted(position);
    }

    public void add(T data) {
        add(mDataList.size(), data);
    }

    public void addAll(int positionStart, List<T> dataList) {
        mDataList.addAll(positionStart, dataList);
        notifyItemRangeInserted(positionStart, dataList.size());
    }

    public void addAll(List<T> dataList) {
        addAll(mDataList.size(), dataList);
    }

    public void move(int fromPosition, int toPosition) {
        T remove = mDataList.remove(fromPosition);
        mDataList.add(toPosition, remove);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void change(int position, T data) {
        mDataList.remove(position);
        mDataList.add(position, data);
        notifyItemChanged(position);
    }

    public void remove(int position) {
        mDataList.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }
}
