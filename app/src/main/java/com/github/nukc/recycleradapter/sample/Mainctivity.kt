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
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.holder.Holder
import com.bumptech.glide.Glide
import com.github.nukc.recycleradapter.MultiTypeProvider
import com.github.nukc.recycleradapter.PureLayoutProvider
import com.github.nukc.recycleradapter.RecyclerAdapter
import com.github.nukc.recycleradapter.SimpleProvider
import com.github.nukc.recycleradapter.sample.model.Banner
import com.github.nukc.recycleradapter.sample.model.Chosen
import com.github.nukc.recycleradapter.sample.model.NumberItem
import kotlinx.android.synthetic.main.activity_main.*

class Mainctivity : AppCompatActivity() {

    private var adapter: RecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
    }

    private fun setup() {
        val imageUrl = "https://github.com/nukc/RecyclerAdapter/blob/master/art/banner.png"
        val banners: List<Banner> = mutableListOf(Banner(imageUrl))
        val chosen = Chosen(imageUrl, "Chosen")

        recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerAdapter.explosion()
                .register(bannerProvider)
                .register(chosenProvider)
                .register(object : PureLayoutProvider<Int>(Integer::class.java) {
                    override fun getLayoutResId(): Int {
                        return R.layout.item_pure
                    }

                    override fun initHolder(holder: RecyclerView.ViewHolder, itemView: View) {
                        itemView.findViewById<View>(R.id.layout_likest).setOnClickListener {
                            Toast.makeText(this@Mainctivity, "每日最赞", Toast.LENGTH_SHORT).show()

                        }
                        itemView.findViewById<View>(R.id.layout_editor_picks).setOnClickListener {
                            Toast.makeText(this@Mainctivity, "编辑精选", Toast.LENGTH_SHORT).show()

                        }
                        itemView.findViewById<View>(R.id.tv_more).setOnClickListener {
                            Toast.makeText(this@Mainctivity, "更多", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
                .register(multiProvider)
                .setItems(mutableListOf(banners, mutableListOf(chosen, chosen, chosen), 1))
                .build()
        adapter?.setHasStableIds(true)
        recycler_view.adapter = adapter

        recycler_view.postDelayed({
            val numberItems = mutableListOf<NumberItem>()
            for (i in 0..5) {
                numberItems.add(NumberItem(i))
            }
            adapter?.addAll(numberItems.toMutableList())
        }, 500)
    }

    private val bannerProvider = object : SimpleProvider<List<Banner>>(Banner::class.java) {

        override fun getLayoutResId(): Int {
            return R.layout.view_banner
        }

        override fun bind(holder: RecyclerView.ViewHolder, data: List<Banner>) {
            val banner = holder.itemView.findViewById<ConvenientBanner<Any>>(R.id.convenient_banner)
            if (banner.childCount > 0 && banner.getChildAt(0) is ViewGroup) {
                val viewPager = banner.getChildAt(0) as ViewGroup
                if (viewPager.childCount > 0) {
                    return
                }

            }
            // setup banner
            banner.setPages(creator, data)
                    ?.setOnItemClickListener { position ->
                        val item = data[position]
                        Toast.makeText(this@Mainctivity, item.image, Toast.LENGTH_SHORT).show()
                    }
                    ?.setCanLoop(true)
                    ?.startTurning(3000)
                    ?.setPageIndicator(intArrayOf(R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused))
        }

        private val creator = object : CBViewHolderCreator {
            override fun createHolder(itemView: View): Holder<Banner> {
                return LocalImageHolderView(itemView)
            }

            override fun getLayoutId(): Int {
                return R.layout.item_banner
            }
        }

        private inner class LocalImageHolderView(itemView: View) : Holder<Banner>(itemView) {
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
    }

    private val chosenProvider = object
        : SimpleProvider<List<Chosen>>(Chosen::class.java) {

        private var viewPager: ViewPager? = null

        override fun initHolder(holder: RecyclerView.ViewHolder, itemView: View) {
            viewPager = itemView.findViewById(R.id.view_pager)
            viewPager?.setPageTransformer(true, AlphaAndScalePageTransformer())
            viewPager?.pageMargin = Utils.dip2px(itemView.context, 15f)
        }

        override fun bind(holder: RecyclerView.ViewHolder, data: List<Chosen>) {
            if (viewPager?.adapter == null) {
                viewPager?.adapter = ChosenPagerAdapter(data)
                viewPager?.offscreenPageLimit = data.size - 1
                if (data.size > 3) {
                    viewPager?.post {
                        viewPager?.currentItem = 1
                    }
                }
            }

        }

        override fun getLayoutResId(): Int {
            return R.layout.item_chosen
        }

        private inner class ChosenPagerAdapter(var list: List<Chosen>) : PagerAdapter() {
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

    private val multiProvider = object
        : MultiTypeProvider<NumberItem, MultiHolder>(NumberItem::class.java) {
        override fun providerAllLayoutResId(): IntArray {
            return intArrayOf(R.layout.item_left, R.layout.item_right)
        }

        override fun getLayoutResId(position: Int, data: NumberItem): Int {
            return when (data.number % 2) {
                0 -> R.layout.item_left
                else -> R.layout.item_right
            }
        }

        override fun provideHolder(itemView: View): MultiHolder {
            return MultiHolder(itemView)
        }

        override fun bind(holder: MultiHolder, data: NumberItem) {
            holder.data = data
            holder.tvText.text = data.number.toString()
        }

    }

    private class MultiHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvText: TextView = view.findViewById(R.id.tv_text)

        var data: NumberItem? = null

        init {
            itemView.setOnClickListener {
                data?.let {

                }
            }
        }
    }
}
