package com.nikola.covernews.data.network

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.nikola.covernews.internal.NoConnectionException
import com.nikola.covernews.internal.isOnline
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptorImpl(
    context: Context
) : ConnectivityInterceptor {

    private val appContext = context.applicationContext

    @RequiresApi(Build.VERSION_CODES.M)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline(appContext))
            throw NoConnectionException()
        return chain.proceed(chain.request())
    }
}