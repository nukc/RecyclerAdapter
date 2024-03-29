# RecyclerAdapter

> 简单易懂的 RecyclerView 通用 adapter 封装

<p align="center">
    <a href="https://bintray.com/nukc/maven/RecyclerAdapter/_latestVersion"><img src="https://img.shields.io/bintray/v/nukc/maven/RecyclerAdapter.svg?style=flat-square"></a>
    <a href="https://bintray.com/nukc/maven/RecyclerAdapter-DSL/_latestVersion"><img src="https://img.shields.io/badge/dsl-1.0-orange?style=flat-square"></a>
    <a href="https://travis-ci.org/nukc/RecyclerAdapter"><img src="https://img.shields.io/travis/nukc/RecyclerAdapter.svg?style=flat-square"/></a>
    <a href="https://github.com/nukc/recycleradapter/blob/master/LICENSE"><img src="https://img.shields.io/badge/license-Apache-757575.svg?style=flat-square"/></a>
</p>


## Intention

需要迁移到 AndroidX，封装实现方法很简单，代码也很少。

<img src="https://raw.githubusercontent.com/nukc/RecyclerAdapter/kotlin/art/10e.jpg" width="200">

## Installation

Add it in your root build.gradle at the end of repositories:
```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

add the dependency to your build.gradle:

kotlinx.android.synthetic (LayoutContainer) Version: [v1.0](https://github.com/nukc/RecyclerAdapter/blob/dsl/README-synthetic.md)

```groovy
    // ViewBinding Version
    implementation 'com.github.nukc:RecyclerAdapter:dsl-v1.2.1'
```

## Usage

> DSL 写法

```kotlin
// 返回 adapter
recycler_view.setup(LinearLayoutManager(this)) {
    hasStableIds = true
    
    renderItem<Int, ItemPureBinding> {
    	res(R.layout.item_pure)
        getViewBinding = {
            ItemPureBinding.bind(it)
        }
    }
    
    renderItem<NumberItem, ItemLeftBinding> {
        res(R.layout.item_left, R.layout.item_right) // 支持同个类型多个布局
        getViewBinding = {
            ItemLeftBinding.bind(it)
        }
        getItemViewType {
            when (data.number % 2) {
                0 -> R.layout.item_left
                else -> R.layout.item_right
            }
        }
        bind {
            // 可在这里更新视图
            binding.tvText.text = data.number.toString()
        }
        partialUpdate {
            // payloads 不为空的时候，在此局部更新
        }
    }
    
    renderItem<List<Banner>, ViewBannerBinding> {
        type = Banner::class.java  // 当 item 的数据是数组，需要设置 type
        getViewBinding = {
            ViewBannerBinding.bind(it)
        }
        res(R.layout.view_banner) {
            // 可在这里对试图进行设置，比如点击事件
        }
        bind {
           // ...
        }
        
        // ... 还有对应 Adapter 的其它一些方法
    }
    
}
```



> 常规写法

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

```java
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
```
```kotlin
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
