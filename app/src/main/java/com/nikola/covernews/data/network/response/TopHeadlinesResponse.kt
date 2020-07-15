package com.nikola.covernews.data.network.response

import com.nikola.covernews.data.model.Article

data class TopHeadlinesResponse(
    val status: String = "",
    val totalResults: Int = 0,
    val articles: List<Article> = listOf()
)