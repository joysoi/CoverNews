package com.example.covernews.ui.main.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.covernews.data.network.NewsApiResponseService

class SharedViewModelFactory(
    private val newsApiResponseService: NewsApiResponseService,
    private val application: Application
): ViewModelProvider.NewInstanceFactory(){
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SharedViewModel(newsApiResponseService, application) as T
    }
}