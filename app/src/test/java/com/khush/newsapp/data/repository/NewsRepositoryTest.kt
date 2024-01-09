package com.khush.newsapp.data.repository

import com.khush.newsapp.common.util.apiArticleListToArticleList
import com.khush.newsapp.data.database.DatabaseService
import com.khush.newsapp.data.database.entity.Source
import com.khush.newsapp.data.model.ApiArticle
import com.khush.newsapp.data.model.News
import com.khush.newsapp.data.network.ApiInterface
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NewsRepositoryTest {

    @Mock
    private lateinit var apiInterface: ApiInterface

    @Mock
    private lateinit var database: DatabaseService

    private lateinit var newsRepository: NewsRepository

    @Before
    fun setUp() {
        newsRepository = NewsRepository(database, apiInterface)
    }

    @Test
    fun getNews_whenNetworkServiceResponseSuccess_shouldReturnSuccess() {
        runTest {
            val source = Source(id = "sourceId", name = "sourceName")
            val article = ApiArticle(
                source = source,
                title = "title",
                description = "description",
                url = "url",
                urlToImage = "urlToImage",
                author = "author",
                content = "content",
                publishedAt = "pat"
            )

            val articles = mutableListOf<ApiArticle>()
            articles.add(article)

            val response = News(
                status = "ok", totalResults = 1, articles = articles
            )

            Mockito.doReturn(response).`when`(apiInterface).getNews()
            Mockito.doReturn(
                flowOf(
                    response.articles.apiArticleListToArticleList()
                )
            )
                .`when`(database).getAllArticles()

            val actual = newsRepository.getNews()
            assertEquals(response.articles.apiArticleListToArticleList(), actual)

        }
    }

    @Test
    fun getTopHeadlines_whenNetworkServiceResponseError_shouldReturnError() {
        runTest {
            val errorMessage = "Error Message"

            Mockito.doThrow(RuntimeException(errorMessage)).`when`(apiInterface).getNews()

            assertThrows(RuntimeException::class.java) {
                runBlocking {
                    newsRepository.getNews()
                }
            }
        }
    }
}