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

        public ItemHolder(View itemView, OnItemListener listener) {
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

监听点击事件
```java
    interface OnItemListener {
        void onItemClick(View view, int position);
    }

    private OnItemListener mItemListener = new OnItemListener() {
        @Override
        public void onItemClick(View view, int position) {
            NumberItem numberItem = mNumberList.get(position);
            Toast.makeText(MainActivity.this, "number=" + numberItem.getNumber(), Toast.LENGTH_SHORT).show();
        }
    };
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

```

## About

Adapter 方法说明

> 对数据操作的方法，最后都会 notify

方法名 | 备注
:------------- | :-------------
getItem(position) | 获取对应的 item
getItemCount() | 获取 item 的数量
getDataList() | 获取数据集合
refresh(dataList) | 清空原先的数据再加入新的数据后刷新
add(position, data) | 在指定位置插入
add(data) | 在最后位置插入
addAll(positionStart, dataList) | 在指定开始位置插入一个集合
addAll(dataList) | 在最后位置插入一个集合
move(fromPosition, toPosition) | 把 fromPosition 的 item 移动到 toPosition
change(position, data) | 改变指定位置的数据，然后刷新 item
remove(position) | 移除指定位置的 item
clear() | 清空
setItemListener(listener) | 设置监听，也可在构造方法中传入
getItemHolder(position) | 获取指定位置的 ItemWrapper ，该方法由 ItemProvide 接口声明，继承 RecyclerAdapter 需要实现该方法

ItemWrapper 说明

> 该类用于控制 itemView 的 Type

变量成员 | 备注
:------------- | :-------------
mLayoutResId | 布局文件Id
mHolderClass | RecyclerHolder 的子类 Class

欢迎大家 PR 、提 issues ，一起加入更多方便实用的方法。

## Proguard

```
-keepclassmembers public class * extends com.github.nukc.recycleradapter.RecyclerHolder {
    public <init>(...);
}
```

## Thanks

- [StanKocken/EfficientAdapter](https://github.com/StanKocken/EfficientAdapter)  封装用了反射，就是看到这个库才想到反射的。

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