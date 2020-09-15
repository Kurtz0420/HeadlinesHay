package com.client.headlineshay.utils

import android.content.res.Resources
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.client.headlineshay.R
import com.client.headlineshay.network.models.local.ArticleLocal
import kotlinx.android.synthetic.main.headlines_pager_item_layout.view.*
import kotlinx.android.synthetic.main.layout_article_item_holder.view.*
import kotlinx.android.synthetic.main.layout_headlines_holder.view.*


/*Adapter with Optimized DiffUtil : uses background thread to carry out calculations for view update*/

class ArticlesListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ARTICLE_VIEW_TYPE:Int = 0;
    private val LOADING_VIEW_TYPE:Int = 1;
    private val HEADLINES_VIEW_TYPE:Int = 2;
    private val loading_indicator:String = "Loading"
    private val HEADLINES_SIZE = 6
    private lateinit var headlinesList:MutableList<ArticleLocal>


    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticleLocal>() {

        override fun areItemsTheSame(oldItem: ArticleLocal, newItem: ArticleLocal): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ArticleLocal, newItem: ArticleLocal): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        if(viewType == HEADLINES_VIEW_TYPE){

            return HeadlinesPagerHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.headlines_pager_item_layout,
                    parent,
                    false
                ),
                interaction
            )
        }

        if(viewType == LOADING_VIEW_TYPE){
            //show loading
            return LoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.layout_loading_holder,
                    parent,
                    false
                )
            )

        }else{
            //show article

            return ArticleViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.layout_article_item_holder,
                    parent,
                    false
                ),
                interaction
            )

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ArticleViewHolder -> {
                Log.d("ArticlesAdapter", "onBindViewHolder: Positions with headlines : $position")
                holder.bind(differ.currentList[position])
            }
            is LoadingViewHolder -> {
                holder.bind()
            }
            is HeadlinesPagerHolder ->{
//                val list = listOf(differ.currentList[position], differ.currentList[position+1], differ.currentList[position+2], differ.currentList[position+3], differ.currentList[position+4], differ.currentList[position+5]).toMutableList()
//                holder.bind(differ.currentList.take(HEADLINES_SIZE).toMutableList())
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        if(position <= HEADLINES_SIZE){
            return HEADLINES_VIEW_TYPE
        }

        return if(differ.currentList[position].title == loading_indicator){
            LOADING_VIEW_TYPE
        }else{
            ARTICLE_VIEW_TYPE
        }
    }

    override fun getItemCount(): Int {

        return differ.currentList.size
    }

    fun submitList(list: List<ArticleLocal>) {
        differ.submitList(list)
    }




    class LoadingViewHolder
    constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {


        fun bind() = with(itemView) {


        }




    }

    class HeadlinesPagerHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {


        fun bind(item: ArticleLocal) = with(itemView) {

            itemView.setOnClickListener {
                interaction?.onHeadlineSelected(adapterPosition, item)
            }


            itemView.rootCard.requestLayout()
            val newHeight = Resources.getSystem().displayMetrics.heightPixels / 2 + 300
            itemView.rootCard.layoutParams.height = newHeight

            val requestOptions = RequestOptions
                .placeholderOf(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .override(450,450)

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(item.urlToImage).into(itemView.image_pager_item)
            itemView.title_pager_item.text = item.title
            itemView.subtitle_pager_item.text = item.description


//            this.headlines_pager.requestLayout()
//            val newHeight = Resources.getSystem().displayMetrics.heightPixels / 2 + 300
//            this.headlines_pager.layoutParams.height = newHeight
//            this.headlines_pager.adapter = HeadlinesPagerAdapter(context, items.toMutableList())
//            this.dots_indicator.setViewPager(this.headlines_pager)

        }




    }


    class ArticleViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ArticleLocal) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            itemView.root.requestLayout()
            val newHeight = Resources.getSystem().displayMetrics.heightPixels / 2 - 100
            itemView.root.layoutParams.height = newHeight

            itemView.title_item.text = item.title
            itemView.decription_item.text = item.description
//            itemView.url_item.text = item.url

            val requestOptions = RequestOptions
                .placeholderOf(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .override(350,350)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(item.urlToImage)
                .into(itemView.iv)


//            TODO("bind view with data")
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: ArticleLocal)
        fun onHeadlineSelected(position: Int, item:ArticleLocal)
    }


}