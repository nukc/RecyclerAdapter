package com.github.nukc.recycleradapter.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.holder.Holder
import com.bumptech.glide.Glide
import com.github.nukc.recycleradapter.RecyclerAdapter
import com.github.nukc.recycleradapter.dsl.setup
import com.github.nukc.recycleradapter.sample.model.Banner
import com.github.nukc.recycleradapter.sample.model.Chosen
import com.github.nukc.recycleradapter.sample.model.NumberItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_chosen.*
import kotlinx.android.synthetic.main.item_left.*
import kotlinx.android.synthetic.main.item_pure.*
import kotlinx.android.synthetic.main.view_banner.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
    }

    private fun setup() {
        val imageUrl = "https://raw.githubusercontent.com/nukc/RecyclerAdapter/kotlin/art/banner.png"
        val banners: List<Banner> = mutableListOf(Banner(imageUrl))
        val chosen = Chosen(imageUrl, "Chosen")

        adapter = recycler_view.setup(LinearLayoutManager(this)) {
            hasStableIds = true

            renderItem<List<Banner>> {
                type = Banner::class.java
                res(R.layout.view_banner)

                bind {
                    if (convenient_banner.childCount > 0 && convenient_banner.getChildAt(0) is ViewGroup) {
                        val viewPager = convenient_banner.getChildAt(0) as ViewGroup
                        if (viewPager.childCount > 0) {
                            return@bind
                        }
                    }
                    // setup banner
                    (convenient_banner as ConvenientBanner<Any>).setPages(creator, data)
                            ?.setOnItemClickListener { position ->
                                val item = data[position]
                                Toast.makeText(this@MainActivity, item.image, Toast.LENGTH_SHORT).show()
                            }
                            ?.setCanLoop(true)
                            ?.startTurning(3000)
                            ?.setPageIndicator(intArrayOf(R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused))
                }
            }

            renderItem<List<Chosen>> {
                type = Chosen::class.java
                res(R.layout.item_chosen) {
                    view_pager.setPageTransformer(true, AlphaAndScalePageTransformer())
                    view_pager.pageMargin = Utils.dip2px(itemView.context, 15f)
                }

                bind {
                    if (view_pager.adapter == null) {
                        view_pager.adapter = ChosenPagerAdapter(data)
                        view_pager.offscreenPageLimit = data.size - 1
                        if (data.size > 3) {
                            view_pager.post {
                                view_pager.currentItem = 1
                            }
                        }
                    }
                }
            }

            renderItem<Int> {
                res(R.layout.item_pure) {
                    layout_likest.setOnClickListener {
                        Toast.makeText(this@MainActivity, "每日最赞", Toast.LENGTH_SHORT).show()
                    }
                    layout_editor_picks.setOnClickListener {
                        Toast.makeText(this@MainActivity, "编辑精选", Toast.LENGTH_SHORT).show()
                    }
                    tv_more.setOnClickListener {
                        Toast.makeText(this@MainActivity, "更多", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            renderItem<NumberItem> {
                res(R.layout.item_left, R.layout.item_right)

                getItemViewType {
                    when (data.number % 2) {
                        0 -> R.layout.item_left
                        else -> R.layout.item_right
                    }
                }

                bind {
                    tv_text.text = data.number.toString()
                }
            }
        }

        adapter.addAll(mutableListOf(banners, mutableListOf(chosen, chosen, chosen), 1))
        recycler_view.postDelayed({
            val numberItems = mutableListOf<NumberItem>()
            for (i in 0..5) {
                numberItems.add(NumberItem(i))
            }
            adapter.addAll(numberItems.toMutableList())
        }, 500)
    }

    private val creator = object : CBViewHolderCreator {
        override fun createHolder(itemView: View): Holder<Banner> {
            return LocalImageHolderView(itemView)
        }

        override fun getLayoutId(): Int {
            return R.layout.item_banner
        }
    }

    private class LocalImageHolderView(itemView: View) : Holder<Banner>(itemView) {
        private var imageView: ImageView? = null

        override fun initView(itemView: View) {
            imageView = itemView.findViewById(R.id.iv_banner)
        }

        override fun updateUI(data: Banner) {
            imageView?.let {
                Glide.with(it)
                        .load(data.image)
                        .into(it)
            }
        }
    }

    private class ChosenPagerAdapter(var list: List<Chosen>) : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return list.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(container.context)
                    .inflate(R.layout.item_chosen_child, container, false)
            val data = list[position]

            Glide.with(container)
                    .load(data.image)
                    .into(view.findViewById(R.id.iv_cover))
            view.findViewById<TextView>(R.id.tv_title).text = data.title
            container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View?)
        }

    }

}
