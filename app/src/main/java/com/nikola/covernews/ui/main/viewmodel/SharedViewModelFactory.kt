package com.nikola.covernews.ui.main.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nikola.covernews.data.network.NewsApiResponseService

class SharedViewModelFactory(
    private val newsApiResponseService: NewsApiResponseService
): ViewModelProvider.NewInstanceFactory(){
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SharedViewModel(newsApiResponseService) as T
    }
}