package com.khush.newsapp.data.repository

import com.khush.newsapp.common.Const
import com.khush.newsapp.common.Const.DEFAULT_PAGE_NUM
import com.khush.newsapp.common.util.apiArticleListToArticleList
import com.khush.newsapp.common.util.apiSourceListToSourceList
import com.khush.news.data.database.DatabaseService
import com.khush.newsapp.data.database.entity.Article
import com.khush.newsapp.data.database.entity.Source
import com.khush.newsapp.data.model.Country
import com.khush.newsapp.data.model.Language
import com.khush.newsapp.data.network.ApiInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val database: DatabaseService,
    private val network: ApiInterface
) {

    suspend fun getNewsByCountry(
        countryCode: String,
        pageNumber: Int = DEFAULT_PAGE_NUM
    ): Flow<List<Article>> = flow {
        emit(
            network.getNews(
                countryCode,
                pageNum = pageNumber
            ).articles.apiArticleListToArticleList()
        )
    }

    suspend fun getNewsByLanguage(
        languageCode: String,
        pageNumber: Int = DEFAULT_PAGE_NUM
    ): Flow<List<Article>> = flow {
        emit(
            network.getNewsByLang(
                languageCode,
                pageNum = pageNumber
            ).articles.apiArticleListToArticleList()
        )
    }

    suspend fun getNewsBySource(
        sourceCode: String,
        pageNumber: Int = DEFAULT_PAGE_NUM
    ): Flow<List<Article>> = flow {
        emit(
            network.getNewsBySource(
                sourceCode,
                pageNum = pageNumber
            ).articles.apiArticleListToArticleList()
        )
    }

    suspend fun searchNews(
        searchQuery: String,
        pageNumber: Int = DEFAULT_PAGE_NUM
    ): Flow<List<Article>> =
        flow {
            emit(
                network.searchNews(searchQuery, pageNumber).articles.apiArticleListToArticleList()
            )
        }

    suspend fun upsert(article: Article) {
        database.upsert(article)
    }

    fun getSavedNews(): Flow<List<Article>> = database.getSavedArticles()

    suspend fun deleteArticle(article: Article) = database.deleteArticle(article)

    suspend fun getSources(): Flow<List<Source>> = flow {
        emit(
            network.getSources().sources.apiSourceListToSourceList()
        )
    }

    suspend fun getCountries(): Flow<List<Country>> = flow {
        emit(Const.countryList)
    }

    suspend fun getLanguages(): Flow<List<Language>> = flow {
        emit(Const.languageList)
    }
}