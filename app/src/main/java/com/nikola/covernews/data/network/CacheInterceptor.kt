package com.nikola.covernews.data.network

import okhttp3.Cache
import okhttp3.Interceptor

interface CacheInterceptor: Interceptor {
    val myCache: Cache
}