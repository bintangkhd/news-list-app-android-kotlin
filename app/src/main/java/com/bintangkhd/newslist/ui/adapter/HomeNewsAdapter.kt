package com.bintangkhd.newslist.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bintangkhd.newslist.R
import com.bintangkhd.newslist.data.model.NewsItem
import com.bintangkhd.newslist.data.service.local.NewsHistory
import com.bintangkhd.newslist.databinding.ItemHomeNewsBinding
import com.bintangkhd.newslist.utils.Utilities
import com.bumptech.glide.Glide

class HomeNewsAdapter(private val onItemClick: (NewsHistory) -> Unit) :
    PagingDataAdapter<NewsItem, HomeNewsAdapter.NewsViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemHomeNewsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NewsViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = getItem(position)
        Log.d("HomeNewsAdapter", "Binding item at position: $position, data: $newsItem")
        newsItem?.let { holder.bind(it) }
    }

    class NewsViewHolder(
        private val binding: ItemHomeNewsBinding,
        private val onItemClick: (NewsHistory) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: NewsItem) {

            binding.tvHomeNewsTitle.text = news.title
//            binding.tvHomeNewsDesc.text = news.description ?: "No description available"
            binding.tvHomeNewsDesc.text = Utilities.truncateDesc(news.description, 100)
            binding.tvHomeNewsName.text = news.source.name
            binding.tvHomeNewsAuthor.text = news.author ?: "Unknown Author"
            binding.tvHomeNewsDate.text = Utilities.formatDate(news.publishedAt)

            Glide.with(binding.ivHomeNews.context)
                .load(news.urlToImage)
                .placeholder(R.drawable.ic_sample_photo)
                .into(binding.ivHomeNews)

            binding.root.setOnClickListener {
                val newsHistory = NewsHistory(
                    url = news.url,
                    title = news.title,
                    imageUrl = news.urlToImage
                )
                onItemClick(newsHistory)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsItem>() {
            override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}
