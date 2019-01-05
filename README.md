# RecyclerAdapter

> 简单易懂的 RecyclerView 通用 adapter 封装

<p align="center">
    <a href="https://travis-ci.org/nukc/recycleradapter"><img src="https://img.shields.io/travis/nukc/recycleradapter.svg?style=flat-square"/></a>
    <a href="https://github.com/nukc/recycleradapter/blob/master/LICENSE"><img src="https://img.shields.io/badge/license-Apache-757575.svg?style=flat-square"/></a>
</p>


## Intention

2 年前的旧版本是用反射写的，当时写的时候就是想用不同于其它库的实现方法写一个，但是由于是反射，平常使用是没有问题，但总是感觉不好，
后来又更多的开始在写 Android 了，Kotlin 也出正式版了，就捣鼓了一下。这个版本实现方法很简单，代码也很少。


## Usage

```java
recycler_view.adapter = RecyclerAdapter.explosion()
            .register(bannerProvider)
            .register(chosenProvider)
            .register(object : PureLayoutProvider<Int>(Integer::class.java) {
                override fun getLayoutResId(): Int {
                    return R.layout.item_pure
                }

                override fun initHolder(holder: RecyclerView.ViewHolder, itemView: View) {
                    itemView.findViewById<View>(R.id.layout_likest).setOnClickListener {
                        //
                    }
                    itemView.findViewById<View>(R.id.layout_editor_picks).setOnClickListener {
                        //
                    }
                    itemView.findViewById<View>(R.id.tv_more).setOnClickListener {
                        //
                    }
                }
            })
            .register(multiProvider)
            .setItems(arrayListOf(banners, arrayListOf(chosen), 1))
            .build()

```


## License

Apache License, Version 2.0