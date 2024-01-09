package com.khush.newsapp.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.paging.Pager
import app.cash.turbine.test
import com.khush.newsapp.common.Const
import com.khush.newsapp.common.dispatcher.DispatcherProvider
import com.khush.newsapp.common.dispatcher.TestDispatcherProvider
import com.khush.newsapp.common.logger.Logger
import com.khush.newsapp.common.logger.TestLogger
import com.khush.newsapp.common.networkhelper.NetworkHelper
import com.khush.newsapp.common.networkhelper.TestNetworkHelper
import com.khush.newsapp.data.database.entity.Article
import com.khush.newsapp.data.repository.NewsRepository
import com.khush.newsapp.ui.base.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class NewsViewModelTest {

    @Mock
    private lateinit var newsRepository: NewsRepository

    @Mock
    private lateinit var newsPagingSource: Pager<Int, Article>

    private lateinit var logger: Logger
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var networkHelper: NetworkHelper

    @Before
    fun setUp() {
        logger = TestLogger()
        dispatcherProvider = TestDispatcherProvider()
        networkHelper = TestNetworkHelper()
        Dispatchers.setMain(dispatcherProvider.main)
    }

    @Test
    fun fetchNewsByCountry_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            doReturn(flowOf(emptyList<Article>()))
                .`when`(newsRepository)
                .getNewsByCountry(Const.DEFAULT_COUNTRY)
            val savedStateHandle = SavedStateHandle().apply {
                set("country", Const.DEFAULT_COUNTRY)
            }
            val viewModel = NewsViewModel(
                savedStateHandle,
                newsRepository,
                newsPagingSource,
                logger,
                dispatcherProvider,
                networkHelper
            )
            viewModel.newsItem.test {
                assertEquals(UIState.Success(emptyList<Article>()), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(newsRepository, Mockito.times(1)).getNewsByCountry(Const.DEFAULT_COUNTRY)
        }
    }

    @Test
    fun fetchNewsByCountry_whenRepositoryResponseError_shouldSetErrorUiState() {
        runTest {
            val errorMessage = "Error Message"
            val exception = IllegalStateException(errorMessage)
            doReturn(flow<List<Article>> {
                throw exception
            })
                .`when`(newsRepository)
                .getNewsByCountry(Const.DEFAULT_COUNTRY)
            val savedStateHandle = SavedStateHandle().apply {
                set("country", Const.DEFAULT_COUNTRY)
            }
            val viewModel = NewsViewModel(
                savedStateHandle,
                newsRepository,
                newsPagingSource,
                logger,
                dispatcherProvider,
                networkHelper
            )
            viewModel.newsItem.test {
                assertEquals(
                    UIState.Failure(exception, null).toString(),
                    awaitItem().toString()
                )
                cancelAndIgnoreRemainingEvents()
            }
            verify(newsRepository, Mockito.times(1)).getNewsByCountry(Const.DEFAULT_COUNTRY)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

}