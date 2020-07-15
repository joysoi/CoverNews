package com.nikola.covernews.data.network

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.nikola.covernews.internal.isOnline
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class CacheInterceptorImpl(context: Context) : CacheInterceptor {
    private val appContext = context.applicationContext
    override val myCache: Cache
        get() = _myCache

    @RequiresApi(Build.VERSION_CODES.M)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (!isOnline(appContext)) {
            val cacheControl: CacheControl = CacheControl.Builder()
                .maxStale(15, TimeUnit.MINUTES)
                .build()

            request.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .cacheControl(cacheControl)
                .build()

        }

        return chain.proceed(request)
    }

    private val cacheSize = (10 x 1024 x 1024).toLong()

    private infix fun Int.x(i: Int): Int {
        return this.times(i)
    }

    private val _myCache = Cache(appContext.cacheDir, cacheSize)
}