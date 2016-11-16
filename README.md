# RecyclerAdapter
在想着尽可能保留原味，又想简化代码工作量的情况下封装了RecyclerView.Adapter。

## Installation

add the dependency to your build.gradle:
```
    compile 'com.github.nukc:recycleradapter:0.1'
```

## Usage

```java

    recyclerView.setAdapter(new RecyclerAdapter<NumberItem>(mItemListener) {
        @Override
        public ItemWrapper getItemHolder(int position) {
            // 可获取到对应的model，可根据实际需求返回不一样的Item
            // NumberItem numberItem = getItem(position);
            return new ItemWrapper(R.layout.item, ItemHolder.class);
        }
    });

    private OnItemListener mItemListener = new OnItemListener() {
        @Override
        public void onItemClick(View view, int position) {
            NumberItem numberItem = mNumberList.get(position);
            Toast.makeText(MainActivity.this, "number=" + numberItem.getNumber(), Toast.LENGTH_SHORT).show();
        }
    };

    static class ItemHolder extends RecyclerHolder<NumberItem> {

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

    interface OnItemListener {
        void onItemClick(View view, int position);
    }

```

README待完善。。