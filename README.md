# RecyclerAdapter
在想着尽可能保留原味，又想简化代码工作量的情况下封装了RecyclerView.Adapter。

## Installation

add the dependency to your build.gradle:
```
    compile 'com.github.nukc:recycleradapter:0.1'
```

## Usage

继承 RecyclerHolder 。 (class RecyclerHolder extends RecyclerView.ViewHolder )

在原有的 ViewHolder 上增加了一个抽象方法 onBindView(T t)。

```java
    //NumberItem 是 sample 中的 Model
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
```

点击事件一般是少不了的，声明一个接口
```java
    interface OnItemListener {
        void onItemClick(View view, int position);
    }
```

设置 Adapter
```java

    recyclerView.setAdapter(new RecyclerAdapter<NumberItem>(mItemListener) {
        @Override
        public ItemWrapper getItemHolder(int position) {
            // 可获取到对应的 model ，可根据实际需求返回不一样的 Item
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

```

还有其它方便的方法

README待完善。。

## License

    Copyright 2016, nukc

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.