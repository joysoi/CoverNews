package com.nikola.covernews.data.adapter

import com.nikola.covernews.R
import com.nikola.covernews.data.model.Article
import com.nikola.covernews.internal.dateToTime
import com.nikola.covernews.internal.glide.GlideApp
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item

import kotlinx.android.synthetic.main.item_layout.*
import kotlinx.android.synthetic.main.item_layout.view.*

class NewsItems(val article: Article) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
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

    private fun GroupieViewHolder.bindImg() {
        GlideApp.with(this.containerView)
            .load(article.urlToImage)
            .into(itemView.img)
    }

    private fun GroupieViewHolder.bindTime() {
        time.text = (" \u2022 ${dateToTime(article.publishedAt)}")
    }

}