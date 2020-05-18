package com.example.covernews.ui.main.viewmodel

import android.app.Application
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.covernews.data.network.NewsApiResponseService
import com.example.covernews.data.network.response.TopHeadlinesResponse
import com.example.covernews.internal.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
class CoverViewModel(
    private val newsApiResponseService: NewsApiResponseService,
    application: Application
) : AndroidViewModel(application) {

    private val _newsLiveData = MutableLiveData<TopHeadlinesResponse>()
    val topHeadlinesLiveData: LiveData<TopHeadlinesResponse>
        get() = _newsLiveData

    val news by lazyDeferred {
        try {
            val result = newsApiResponseService.getTopHeadlinesResponse(
                getCountry(),
                setFromDate(),
                setPublishedDate()
            ).await()
            _newsLiveData.postValue(result)
        } catch (e: NoConnectionException) {
            Log.e("Connectivity", "No Internet connection", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.baseContext,
                    "No Internet connection!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun searchNews(string: String): Deferred<Unit> {
        val searchedNews by lazyDeferred {
            try {
                val result = newsApiResponseService.getSearchResults(
                    string,
                    setFromDate(),
                    setPublishedDate()
                ).await()
                _newsLiveData.postValue(result)
            } catch (e: NoConnectionException) {
                Log.e("Connectivity", "No Internet connection", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        getApplication(),
                        "No Internet connection!",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        }
        return searchedNews
    }
}