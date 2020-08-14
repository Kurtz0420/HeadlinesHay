package com.client.headlineshay.utils

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.client.headlineshay.R
import com.client.headlineshay.network.models.local.ArticleLocal
import kotlinx.android.synthetic.main.layout_article_item.view.*


/*Adapter with Optimized DiffUtil : uses background thread to carry out calculations for view update*/

class ArticlesListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_article_item,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ArticleViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<ArticleLocal>) {
        differ.submitList(list)
        val size = differ.currentList.size
        differ.currentList.addAll(list)
        val sizeNew = differ.currentList.size
        notifyItemRangeChanged(size, sizeNew)
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
                .placeholderOf(R.drawable.ic_launcher_background)
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