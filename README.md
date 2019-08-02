# RecyclerAdapter

> 简单易懂的 RecyclerView 通用 adapter 封装

<p align="center">
    <a href="https://bintray.com/nukc/maven/RecyclerAdapter/_latestVersion"><img src="https://img.shields.io/bintray/v/nukc/maven/RecyclerAdapter.svg?style=flat-square"></a>
    <a href="https://travis-ci.org/nukc/RecyclerAdapter"><img src="https://img.shields.io/travis/nukc/RecyclerAdapter.svg?style=flat-square"/></a>
    <a href="https://github.com/nukc/recycleradapter/blob/master/LICENSE"><img src="https://img.shields.io/badge/license-Apache-757575.svg?style=flat-square"/></a>
</p>


## Intention

需要迁移到 AndroidX，封装实现方法很简单，代码也很少。

<img src="https://raw.githubusercontent.com/nukc/RecyclerAdapter/kotlin/art/10e.jpg" width="200">

## Installation

add the dependency to your build.gradle:
```groovy
    implementation 'com.github.nukc:recycleradapter:1.2.0'
```

## Usage

```kotlin
recycler_view.adapter = RecyclerAdapter.explosion()
            .register(bannerProvider)
            .register(chosenProvider)
            .register(object : PureLayoutProvider<Int>(Integer::class.java) {
                override fun getLayoutResId(): Int {
                    return R.layout.item_pure
                }

                override fun initHolder(holder: RecyclerView.ViewHolder, itemView: View) {
                    itemView.findViewById<View>(R.id.layout_likest).setOnClickListener {
                        // do something
                    }
                    // ..
                }
            })
            .register(multiProvider)
            .setItems(arrayListOf(banners, arrayListOf(chosen), 1))
            .build()

```

### Provider

BaseProvider，以下为具体实现类

- PureLayoutProvider 纯布局，不用绑定数据

- SimpleProvider 不想自定义 ViewHolder（在 bind() 方法中 findViewById）

- HolderProvider 大多数需求都可直接用此 Provider

- MultiTypeProvider 遇到同个数据类型且有多个 viewType 的时候（比如聊天界面）


> #### PureLayoutProvider

```kotlin
	object : PureLayoutProvider<Int>(Integer::class.java) {
                override fun getLayoutResId(): Int {
                    return R.layout.item_pure
                }

                override fun initHolder(holder: RecyclerView.ViewHolder, itemView: View) {
                    itemView.findViewById<View>(R.id.layout_likest).setOnClickListener {
                        // do something
                    }
                    // ..
                }
            }
```

> #### MultiProvider

Model and Viewholder

```kotlin
public class NumberItem {
    private int number;

    public NumberItem(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}

private class MultiHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvText: TextView = view.findViewById(R.id.tv_text)
    var data: NumberItem? = null

    init {
        itemView.setOnClickListener {
            data?.let {
                // do something
            }
        }
    }
}
```

provider

```kotlin
    private val multiProvider = object
        : MultiTypeProvider<NumberItem, MultiHolder>(NumberItem::class.java) {
        /**
        * 需要先提供全部类型的 layoutResId
        * @see Builder.register
        */
        override fun providerAllLayoutResId(): IntArray {
            return intArrayOf(R.layout.item_left, R.layout.item_right)
        }

        /**
        * 根据 position 或 data 返回该位置的 layoutResId，同时当做该 item 的 view Type
        * @see RecyclerAdapter.getItemViewType
        */
        override fun getLayoutResId(position: Int, data: NumberItem): Int {
            return when (data.number % 2) {
                0 -> R.layout.item_left
                else -> R.layout.item_right
            }
        }

        /**
        * 可根据 viewType 返回 ViewHolder
        * @param itemView Inflate a new view hierarchy from the viewType
        * @param viewType = getLayoutResId
        */
        override fun provideHolder(itemView: View, viewType: Int): MultiHolder {
            return MultiHolder(itemView)
        }
        
        override fun bind(holder: MultiHolder, data: NumberItem) {
            holder.data = data
            holder.tvText.text = data.number.toString()
        }

    }
```



### Other methods

Provider: 其它一些可覆写方法
```kotlin
fun getItemId(position: Int): Long = position.toLong()
fun onViewRecycled(holder: VH)
fun onFailedToRecycleView(holder: VH): Boolean = false
fun onViewAttachedToWindow(holder: VH)
fun onViewDetachedFromWindow(holder: VH)
```



Adapter: 之后都执行响应的 notify 方法

方法名 | 备注
:------------- | :-------------
refresh(items) | 清空原先的数据再加入新的数据后刷新
add(position, item) | 在指定位置插入
add(item) | 在最后位置插入
addAll(positionStart, items) | 在指定开始位置插入一个集合
addAll(items) | 在最后位置插入一个集合
move(fromPosition, toPosition) | 把 fromPosition 的 item 移动到 toPosition
change(position, item) | 改变指定位置的数据，然后刷新 item
remove(position) | 移除指定位置的 item
clear() | 清空

## License

Apache License, Version 2.0