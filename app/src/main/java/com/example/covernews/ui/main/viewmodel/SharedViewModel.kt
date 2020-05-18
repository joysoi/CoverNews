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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
class SharedViewModel(
    newsApiResponseService: NewsApiResponseService,
    application: Application
) : AndroidViewModel(application) {

    private val _categoryLiveData = MutableLiveData<TopHeadlinesResponse>()
    val categoryLiveData: LiveData<TopHeadlinesResponse>
        get() = _categoryLiveData


    val scienceCategory by lazyDeferred {
        try {
            val result =
                newsApiResponseService.getCategories(
                    "science",
                    getLanguage(),
                    setFromDate(),
                    setPublishedDate()
                )
                    .await()
            _categoryLiveData.postValue(result)
        } catch (e: NoConnectionException) {
            Log.e("Connectivity", "No Internet connection", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(getApplication(), "NO POSTS!", Toast.LENGTH_LONG).show()
            }
        }
    }

    val sportsCategory by lazyDeferred {
        try {
            val result =
                newsApiResponseService.getCategories(
                    "sport",
                    getLanguage(),
                    setFromDate(),
                    setPublishedDate()
                )
                    .await()
            _categoryLiveData.postValue(result)
        } catch (e: NoConnectionException) {
            Log.e("Connectivity", "No Internet connection", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(getApplication(), "NO POSTS!", Toast.LENGTH_LONG).show()
            }
        }
    }

    val businessCategory by lazyDeferred {
        try {
            val result =
                newsApiResponseService.getCategories(
                    "business",
                    getLanguage(),
                    setFromDate(),
                    setPublishedDate()
                )
                    .await()
            _categoryLiveData.postValue(result)
        } catch (e: NoConnectionException) {
            Log.e("Connectivity", "No Internet connection", e)
        }
    }

    val educationCategory by lazyDeferred {
        try {
            val result =
                newsApiResponseService.getCategories(
                    "education",
                    getLanguage(),
                    setFromDate(),
                    setPublishedDate()
                )
                    .await()
            _categoryLiveData.postValue(result)
        } catch (e: NoConnectionException) {
            Log.e("Connectivity", "No Internet connection", e)
        }
    }

    val healthCategory by lazyDeferred {
        try {
            val result =
                newsApiResponseService.getCategories(
                    "health",
                    getLanguage(),
                    setFromDate(),
                    setPublishedDate()
                )
                    .await()
            _categoryLiveData.postValue(result)
        } catch (e: NoConnectionException) {
            Log.e("Connectivity", "No Internet connection", e)
        }
    }

    val technologyCategory by lazyDeferred {
        try {
            val result =
                newsApiResponseService.getCategories(
                    "technology",
                    getLanguage(),
                    setFromDate(),
                    setPublishedDate()
                )
                    .await()
            _categoryLiveData.postValue(result)
        } catch (e: NoConnectionException) {
            Log.e("Connectivity", "No Internet connection", e)
        }
    }

    val travelCategory by lazyDeferred {
        try {
            val result =
                newsApiResponseService.getCategories(
                    "travel",
                    getLanguage(),
                    setFromDate(),
                    setPublishedDate()
                )
                    .await()
            _categoryLiveData.postValue(result)
        } catch (e: NoConnectionException) {
            Log.e("Connectivity", "No Internet connection", e)
        }
    }
}