package com.github.nukc.recycleradapter.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nukc.recycleradapter.ItemWrapper;
import com.github.nukc.recycleradapter.RecyclerAdapter;
import com.github.nukc.recycleradapter.RecyclerHolder;
import com.github.nukc.recycleradapter.sample.model.NumberItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<NumberItem> mNumberList;
    private RecyclerAdapter<NumberItem> mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerAdapter = new RecyclerAdapter<NumberItem>(mItemListener) {
            @Override
            public ItemWrapper getItemHolder(int position) {
                NumberItem numberItem = getItem(position);
                if (numberItem.getNumber() >= 0) {
                    return new ItemWrapper(R.layout.item_positive, PositiveHolder.class);
                } else {
                    return new ItemWrapper(R.layout.item_negative, NegativeHolder.class);
                }
            }
        };
//        mRecyclerAdapter.setItemListener(mItemListener);

        recyclerView.setAdapter(mRecyclerAdapter);

        //模拟网络加载
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 1000);
    }

    private void loadData() {
        mNumberList = new ArrayList<>();
        for (int i = -10; i < 10; i++) {
            int pn = i % 2 == 0 ? 1 : -1;
            mNumberList.add(new NumberItem(i * pn));
        }

        mRecyclerAdapter.addAll(mNumberList);
    }

    private OnItemListener mItemListener = new OnItemListener() {
        @Override
        public void onItemClick(View view, int position) {
            NumberItem numberItem = mNumberList.get(position);
            Toast.makeText(MainActivity.this, "number=" + numberItem.getNumber(), Toast.LENGTH_SHORT).show();
        }
    };

    static class PositiveHolder extends RecyclerHolder<NumberItem> {

        private TextView mTextView;
        private OnItemListener mItemListener;

        public PositiveHolder(View itemView, OnItemListener listener) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text);
            mItemListener = listener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemListener != null) {
                        mItemListener.onItemClick(view, getAdapterPosition());
                    }
                }
            });
        }

        @Override
        public void onBindView(NumberItem item) {
            mTextView.setText("+" + item.getNumber());
        }
    }

    static class NegativeHolder extends RecyclerHolder<NumberItem> {

        private TextView mTextView;

        public NegativeHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text);
        }

        @Override
        public void onBindView(NumberItem item) {
            mTextView.setText(item.getNumber() + "");
        }
    }

    interface OnItemListener {
        void onItemClick(View view, int position);
    }
}
