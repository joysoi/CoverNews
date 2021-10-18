package com.nikola.covernews

import org.junit.Assert
import org.junit.Test

private const val BASE_URL = "https://newsapi.org/v2/"


class URLTest {

    @Test
    fun should_contain_news_in_base_url() {
        val newsString = "news"
        Assert.assertTrue(BASE_URL.contains(newsString))
    }
}