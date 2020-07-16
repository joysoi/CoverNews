package com.nikola.covernews.data.network

import android.util.Log
import com.nikola.covernews.data.network.response.TopHeadlinesResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://newsapi.org/v2/"
const val API_KEY = ""

interface NewsApiResponseService {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("sortBy") sortBy: String,
        @Query("from") from: String
    ): Response<TopHeadlinesResponse>

    @GET("everything")
   suspend fun getSearchResults(
        @Query("q") search: String,
        @Query("language") language: String,
        @Query("sortBy") sortBy: String,
        @Query("from") from: String
    ): Response<TopHeadlinesResponse>

    @GET("everything")
    suspend fun getCategories(
        @Query("q") category: String,
        @Query("language") language: String,
        @Query("sortBy") sortBy: String,
        @Query("from") from: String
    ): Response<TopHeadlinesResponse>

    companion object {
        operator fun invoke(
            cacheInterceptor: CacheInterceptor,
            networkInterceptor: NetworkInterceptor,
            connectivityInterceptor: ConnectivityInterceptor
        ): NewsApiResponseService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter(
                        "apiKey",
                        API_KEY
                    )
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }


            val httpLoggingInterceptor : HttpLoggingInterceptor =
                HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                    Log.d("LoggingInterceptor", it)
                }).setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient = OkHttpClient.Builder()
                .cache(cacheInterceptor.myCache)
                .addInterceptor(connectivityInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(networkInterceptor)
                .addInterceptor(cacheInterceptor)
                .addInterceptor(requestInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(NewsApiResponseService::class.java)
        }
    }
}