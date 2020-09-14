package com.client.headlineshay.utils

import android.content.res.Resources
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.client.headlineshay.R
import com.client.headlineshay.network.models.local.ArticleLocal
import kotlinx.android.synthetic.main.layout_article_item_holder.view.*
import kotlinx.android.synthetic.main.layout_headlines_holder.view.*


/*Adapter with Optimized DiffUtil : uses background thread to carry out calculations for view update*/

class ArticlesListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ARTICLE_VIEW_TYPE:Int = 0;
    private val LOADING_VIEW_TYPE:Int = 1;
    private val HEADLINES_VIEW_TYPE:Int = 2;
    private val loading_indicator:String = "Loading";


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
                    R.layout.layout_headlines_holder,
                    parent,
                    false
                )
            )
        }

        if(viewType == LOADING_VIEW_TYPE){
            Log.d("ArticleListAdapter", "onCreateViewHolder: LOading View Holder")
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
                holder.bind(differ.currentList[position])
            }
            is LoadingViewHolder -> {
                holder.bind()
            }
            is HeadlinesPagerHolder ->{
                holder.bind(listOf(differ.currentList[position], differ.currentList[position+1]).toMutableList())
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(position == 0){
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
//        val size = differ.currentList.size
//        differ.currentList.addAll(list)
//        val sizeNew = differ.currentList.size
//        notifyItemRangeChanged(size, sizeNew)
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
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {


        fun bind(items:MutableList<ArticleLocal>) = with(itemView) {

            this.headlines_pager.requestLayout()
            val newHeight = Resources.getSystem().displayMetrics.heightPixels / 2
            this.headlines_pager.layoutParams.height = newHeight
            this.headlines_pager.adapter = HeadlinesPagerAdapter(context, items.toMutableList())

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

            itemView.title_item.text = item.title
            itemView.decription_item.text = item.description
//            itemView.url_item.text = item.url

            val requestOptions = RequestOptions
                .placeholderOf(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .override(250,250)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(item.urlToImage)
                .into(itemView.iv)


//            TODO("bind view with data")
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: ArticleLocal)
    }
}