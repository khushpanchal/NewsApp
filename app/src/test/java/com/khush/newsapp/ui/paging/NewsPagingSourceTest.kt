package com.khush.newsapp.ui.paging

import androidx.paging.PagingSource
import com.khush.newsapp.common.dispatcher.DispatcherProvider
import com.khush.newsapp.common.dispatcher.TestDispatcherProvider
import com.khush.newsapp.common.networkhelper.NetworkHelper
import com.khush.newsapp.common.networkhelper.TestNetworkHelper
import com.khush.newsapp.data.database.entity.Article
import com.khush.newsapp.data.repository.NewsRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NewsPagingSourceTest {

    @Mock
    private lateinit var newsRepository: NewsRepository

    private lateinit var pagingSource: NewsPagingSource
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var networkHelper: NetworkHelper

    @Before
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()
        networkHelper = TestNetworkHelper()
        pagingSource = NewsPagingSource(newsRepository, networkHelper, dispatcherProvider)
    }

    @Test
    fun load_whenParamCorrect_shouldGiveResult() {
        runTest {
            // Given
            val page = 1
            val articles = emptyList<Article>()

            doReturn(articles)
                .`when`(newsRepository)
                .getNews(page)

            // When
            val result = pagingSource.load(PagingSource.LoadParams.Refresh(page, 1, true))

            // Then
            val expected = PagingSource.LoadResult.Page(
                data = articles,
                prevKey = null,
                nextKey = null
            )

            assertEquals(expected, result)
        }
    }

    @Test
    fun load_whenResponseFailed_shouldGiveError() {
        runTest {
            // Given
            val page = 1
            val error = RuntimeException("Fake error")
            doThrow(error)
                .`when`(newsRepository)
                .getNews(page)

            // When
            val result = pagingSource.load(PagingSource.LoadParams.Refresh(page, 1, false))

            // Then
            val expected = PagingSource.LoadResult.Error<Int, Article>(error)
            assertEquals(expected.toString(), result.toString())
        }
    }

}