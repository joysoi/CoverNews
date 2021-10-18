package com.nikola.covernews.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.nikola.covernews.data.network.NewsApiResponseService
import com.nikola.covernews.data.network.response.TopHeadlinesResponse
import com.nikola.covernews.internal.*
import com.nikola.covernews.ui.main.fragments.*
import kotlinx.coroutines.*
import retrofit2.Response

class SharedViewModel(
    private val newsApiResponseService: NewsApiResponseService
) : ViewModel() {

    private lateinit var isLoadingLiveData: MutableLiveData<Boolean>

    fun isLoading(): LiveData<Boolean> {
        if (!::isLoadingLiveData.isInitialized) {
            isLoadingLiveData = MutableLiveData()
            isLoadingLiveData.value = true
        }
        return isLoadingLiveData
    }

    private lateinit var isErrorLiveData: MutableLiveData<String>

    fun isError(): LiveData<String> {
        if (!::isErrorLiveData.isInitialized) {
            isErrorLiveData = MutableLiveData()
        }
        return isErrorLiveData
    }

    private val _categoryLiveData = MutableLiveData<TopHeadlinesResponse>()
    val categoryLiveData: LiveData<TopHeadlinesResponse>
        get() = _categoryLiveData


    fun transformCategory(categoryByPosition: Int) : String{
         when (categoryByPosition) {
            1 -> {
                return "science"
            }
            2 -> {
                return "sport"
            }
            3 -> {
                return "business"
            }
            4 -> {
                return "education"
            }
            5 -> {
                return "health"
            }
            6 -> {
                return "travel"
            }
            else -> {
                return "technology"
            }
        }
    }

    fun newsCategory(categoryByPosition: Int) {
        Log.d("KURAC", "CATEGORY " + transformCategory(categoryByPosition))
        viewModelScope.launch {
            try {
                isLoadingLiveData.value = true
                withTimeout(5000) {
                    val result by lazyDeferred {
                        val topHeadlines = newsApiResponseService.getCategories(
                            transformCategory(categoryByPosition),
                            getLanguage(),
                            setPeriodDate(),
                            setPublishedDate()
                        )
                        if (topHeadlines.isSuccessful) {
                            topHeadlines.body()?.let {
                                _categoryLiveData.postValue(it)
                            }
                        } else {
                            networkResponseCode(topHeadlines)
                        }
                    }
                    result.await()
                }
            } catch (e: CancellationException) {
                isErrorLiveData.value = "Connection timed out"
            } catch (e: NoConnectionException) {
                isErrorLiveData.value = "No internet connection"
            } finally {
                isLoadingLiveData.value = false
            }
        }
    }

    private fun networkResponseCode(topHeadlines: Response<TopHeadlinesResponse>) {
        when (topHeadlines.code()) {
            403 -> {
                isErrorLiveData.postValue("Resource Forbidden")
            }
            404 -> {
                isErrorLiveData.postValue("Server not found")
            }
            500 -> {
                isErrorLiveData.postValue("Internal Server Error")
            }
            502 -> {
                isErrorLiveData.postValue("Bad Gateway")
            }
            301 -> {
                isErrorLiveData.postValue("Resource Removed")
            }
            302 -> {
                isErrorLiveData.postValue("Removed Resource Found")
            }
            else -> {
                isErrorLiveData.postValue("Network Error")
            }
        }
    }
}