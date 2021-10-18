package com.nikola.covernews.ui.main.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nikola.covernews.data.network.NewsApiResponseService

@Suppress("UNCHECKED_CAST")
class CoverViewModelFactory(
    private val newsApiResponseService: NewsApiResponseService
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CoverViewModel(newsApiResponseService) as T
    }
}