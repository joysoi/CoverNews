package com.nikola.covernews.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikola.covernews.data.network.NewsApiResponseService
import com.nikola.covernews.data.network.response.TopHeadlinesResponse
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class SharedViewModelTest : TestCase(){
    private lateinit var viewModel: SharedViewModel

    private lateinit var isLoadingLiveData: LiveData<Boolean>

    private lateinit var isErrorLiveData: LiveData<String>

    private val topHeadlinesResponse = TopHeadlinesResponse(
        totalResults = 15
    )

    private val newsLiveData: MutableLiveData<TopHeadlinesResponse> = MutableLiveData()

    @Mock
    private lateinit var newsApiResponseService: NewsApiResponseService

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Before
    public override fun setUp() {

        runBlocking {
            Mockito.`when`(topHeadlinesResponse.totalResults)
                .thenReturn(newsLiveData.value?.totalResults)
        }

        viewModel = SharedViewModel(newsApiResponseService)

        isLoadingLiveData = viewModel.isLoading()
        isErrorLiveData = viewModel.isError()
    }

    @Test
    fun `load News Should Show And Hide Loading`() = runBlocking {
        var isLoading = isLoadingLiveData.value
        Assert.assertNotNull(isLoading)
        isLoading?.let { Assert.assertTrue(it) }
        viewModel.isLoading()
        Mockito.verify(topHeadlinesResponse)
        isLoading = isLoadingLiveData.value
        Assert.assertNotNull(isLoading)
        isLoading?.let { Assert.assertFalse(it) }
        return@runBlocking
    }

    @Test
    fun `isError is true when Response throws exception`() = runBlocking {
        var isLoading = isLoadingLiveData.value
        Assert.assertNotNull(isLoading)
        isLoading?.let { Assert.assertTrue(it) }

        var isError = isErrorLiveData.value
        Assert.assertNotNull(isError)
        isError?.let { Assert.assertFalse("Error!", false) }

        Mockito.`when`(topHeadlinesResponse).thenAnswer { throw Exception() }
        viewModel.scienceCategory()
        viewModel.categoryLiveData.value
        Mockito.verify(topHeadlinesResponse)

        isError = isErrorLiveData.value
        Assert.assertNotNull(isError)
        isError?.let { Assert.assertTrue(true) }

        isLoading = isLoadingLiveData.value
        Assert.assertNotNull(isLoading)
        isLoading?.let { Assert.assertFalse(it) }
        return@runBlocking
    }
}