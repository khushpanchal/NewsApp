package com.khush.newsapp.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khush.newsapp.common.Const
import com.khush.newsapp.common.NoInternetException
import com.khush.newsapp.common.dispatcher.DispatcherProvider
import com.khush.newsapp.common.logger.Logger
import com.khush.newsapp.common.networkhelper.NetworkHelper
import com.khush.newsapp.common.util.ValidationUtil.checkIfValidArgNews
import com.khush.newsapp.data.database.entity.Article
import com.khush.newsapp.data.repository.NewsRepository
import com.khush.newsapp.ui.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val newsRepository: NewsRepository,
    private val logger: Logger,
    private val dispatcherProvider: DispatcherProvider,
    private val networkHelper: NetworkHelper,
) : ViewModel() {

    private var pageNum = Const.DEFAULT_PAGE_NUM
    private val _newsItem = MutableStateFlow<UIState<List<Article>>>(UIState.Empty)
    val newsItem: StateFlow<UIState<List<Article>>> = _newsItem

    init {
        fetchNews()
    }

    fun fetchNews() {
        if (checkIfValidArgNews(savedStateHandle.get("country") as? String?)) {
            fetchNewsByCountry(savedStateHandle.get("country"))
        } else if (checkIfValidArgNews(savedStateHandle.get("language") as? String?)) {
            fetchNewsByLanguage(savedStateHandle.get("language"))
        } else if (checkIfValidArgNews(savedStateHandle.get("source") as? String?)) {
            fetchNewsBySource(savedStateHandle.get("source"))
        } else {
            fetchNewsByCountry(countryId = Const.DEFAULT_COUNTRY)
        }
    }

    private fun fetchNewsByCountry(countryId: String?) {
        viewModelScope.launch {
            if (!networkHelper.isNetworkConnected()) {
                _newsItem.emit(
                    UIState.Failure(
                        throwable = NoInternetException()
                    )
                )
                return@launch
            }
            _newsItem.emit(UIState.Loading)
            newsRepository.getNewsByCountry(
                countryId ?: Const.DEFAULT_COUNTRY,
                pageNumber = pageNum
            )
                .mapFilterCollectNews()
        }
    }

    private fun fetchNewsByLanguage(languageId: String?) {
        viewModelScope.launch {
            if (!networkHelper.isNetworkConnected()) {
                _newsItem.emit(
                    UIState.Failure(
                        throwable = NoInternetException()
                    )
                )
                return@launch
            }
            _newsItem.emit(UIState.Loading)
            newsRepository.getNewsByLanguage(
                languageId ?: Const.DEFAULT_LANGUAGE,
                pageNumber = pageNum
            )
                .mapFilterCollectNews()
        }
    }

    private fun fetchNewsBySource(sourceId: String?) {
        viewModelScope.launch {
            if (!networkHelper.isNetworkConnected()) {
                _newsItem.emit(
                    UIState.Failure(
                        throwable = NoInternetException()
                    )
                )
                return@launch
            }
            _newsItem.emit(UIState.Loading)
            newsRepository.getNewsBySource(sourceId ?: Const.DEFAULT_SOURCE, pageNumber = pageNum)
                .mapFilterCollectNews()
        }
    }

    private suspend fun Flow<List<Article>>.mapFilterCollectNews() {
        this.map { item ->
            item.apply {
                this.filter {
                    it.title?.isNotEmpty() == true &&
                            it.urlToImage?.isNotEmpty() == true
                }
            }
        }
            .flowOn(dispatcherProvider.io)
            .catch {
                _newsItem.emit(UIState.Failure(it))
            }
            .collect {
                _newsItem.emit(UIState.Success(it))
                logger.d("NewsViewModel", "Success")
            }
    }

}