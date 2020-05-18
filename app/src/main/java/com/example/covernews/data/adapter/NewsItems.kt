package com.example.covernews.data.adapter

import com.example.covernews.R
import com.example.covernews.data.model.Article
import com.example.covernews.internal.dateToTime
import com.example.covernews.internal.glide.GlideApp
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_layout.*
import kotlinx.android.synthetic.main.item_layout.view.*

class NewsItems(val article: Article) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            author.text = article.author
            textTitle.text = article.title
            desc.text = article.description
            source.text = article.source.name
            bindImg()
            bindTime()
        }
    }

    override fun getLayout() = R.layout.item_layout

    private fun ViewHolder.bindImg() {
        GlideApp.with(this.containerView)
            .load(article.urlToImage)
            .into(itemView.img)
    }

    private fun ViewHolder.bindTime() {
        time.text = (" \u2022 ${dateToTime(article.publishedAt)}")
    }

}