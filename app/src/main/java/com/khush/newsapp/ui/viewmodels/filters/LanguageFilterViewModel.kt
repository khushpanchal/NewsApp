package com.khush.newsapp.ui.viewmodels.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khush.newsapp.common.NoInternetException
import com.khush.newsapp.common.dispatcher.DispatcherProvider
import com.khush.newsapp.common.networkhelper.NetworkHelper
import com.khush.newsapp.data.model.Language
import com.khush.newsapp.data.repository.NewsRepository
import com.khush.newsapp.ui.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageFilterViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val networkHelper: NetworkHelper
) :
    ViewModel() {

    private val _languageItem = MutableStateFlow<UIState<List<Language>>>(UIState.Empty)
    val languageItem: StateFlow<UIState<List<Language>>> = _languageItem

    private val mapLanguages = mutableMapOf<String, Language>()

    init {
        getLanguage()
    }

    fun getLanguage() {
        viewModelScope.launch {
            if (!networkHelper.isNetworkConnected()) {
                _languageItem.emit(
                    UIState.Failure(
                        throwable = NoInternetException()
                    )
                )
                return@launch
            }
            _languageItem.emit(UIState.Loading)
            newsRepository.getLanguages()
                .flowOn(dispatcherProvider.io)
                .catch {
                    _languageItem.emit(UIState.Failure(it))
                }
                .collect {
                    mapLanguages.clear()
                    it.forEach { language ->
                        mapLanguages[language.code] = language.copy()
                    }
                    _languageItem.emit(
                        UIState.Success(
                            mapLanguages.values.toList().map { language -> language.copy() })
                    )
                }
        }
    }
}