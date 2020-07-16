package com.nikola.covernews.ui.main.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikola.covernews.data.network.NewsApiResponseService
import com.nikola.covernews.data.network.response.TopHeadlinesResponse
import com.nikola.covernews.util.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@RunWith(JUnit4::class)
class SharedViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()


    lateinit var sharedViewModel: SharedViewModel

    private lateinit var isLoadingLiveData: LiveData<Boolean>

    private lateinit var isErrorLiveData: LiveData<String>

    @Mock
    lateinit var newsApiResponseService: NewsApiResponseService

    private val topHeadlinesResponse = TopHeadlinesResponse(
        totalResults = 15
    )

    private val categoryLiveData: MutableLiveData<TopHeadlinesResponse> = MutableLiveData()

    @Before
    fun setUp() {
        Mockito.`when`(topHeadlinesResponse.totalResults)
            .thenReturn(categoryLiveData.getOrAwaitValue().totalResults)
        sharedViewModel = SharedViewModel(newsApiResponseService)

        isLoadingLiveData = sharedViewModel.isLoading()
        isErrorLiveData = sharedViewModel.isError()
    }

    @Test
    fun loadNewsShouldShowAndHideLoading() = runBlocking {
        var isLoading = isLoadingLiveData.getOrAwaitValue()
        Assert.assertNotNull(isLoading)
        isLoading.let { Assert.assertTrue(it) }
        sharedViewModel.isLoading()
        Mockito.verify(topHeadlinesResponse)
        isLoading = isLoadingLiveData.getOrAwaitValue()
        Assert.assertNotNull(isLoading)
        isLoading.let { Assert.assertFalse(it) }
        return@runBlocking
    }

    @Test
    fun isErrorTrueWhenResponseThrowsException() = runBlocking {
        var isLoading = isLoadingLiveData.getOrAwaitValue()
        Assert.assertNotNull(isLoading)
        isLoading.let { Assert.assertTrue(it) }

        var isError = isErrorLiveData.getOrAwaitValue()
        Assert.assertNotNull(isError)
        isError.let { Assert.assertFalse("Error!", false) }

        Mockito.`when`(topHeadlinesResponse).thenAnswer { throw Exception() }
        sharedViewModel.scienceCategory()
        sharedViewModel.categoryLiveData.getOrAwaitValue()
        Mockito.verify(topHeadlinesResponse)

        isError = isErrorLiveData.getOrAwaitValue()
        Assert.assertNotNull(isError)
        isError.let { Assert.assertTrue(true) }

        isLoading = isLoadingLiveData.getOrAwaitValue()
        Assert.assertNotNull(isLoading)
        isLoading.let { Assert.assertFalse(it) }
        return@runBlocking
    }
}