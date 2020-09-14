package com.client.headlineshay.utils

/**
 * Created by Gokul on 2/11/2018.
 */
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.client.headlineshay.R
import com.client.headlineshay.network.models.local.ArticleLocal

class HeadlinesPagerAdapter : PagerAdapter {

    lateinit var context: Context
    lateinit var layoutInflater: LayoutInflater ;

    lateinit var itemsList : MutableList<ArticleLocal>

    constructor(
        context: Context,
        itemsList: MutableList<ArticleLocal>
    ) : super() {
        this.context = context
        this.layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.itemsList = itemsList
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {


        val itemView = layoutInflater.inflate(R.layout.headlines_pager_item_layout, container, false);

        val coverImage:ImageView = itemView.findViewById(R.id.image_pager_item)
        val titleTv:TextView = itemView.findViewById(R.id.title_pager_item)
        val subtitleTv:TextView = itemView.findViewById(R.id.subtitle_pager_item)

        val item = itemsList[position]
        Glide.with(context).load(item.urlToImage).into(coverImage)
        titleTv.text = item.title
        subtitleTv.text = item.description


        container.addView(itemView);

        return itemView;
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == (`object` as CardView)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as CardView)
    }

    override fun getCount(): Int {
        return itemsList.size
    }
}