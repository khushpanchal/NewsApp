package com.khush.newsapp.ui.viewmodels.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khush.newsapp.common.NoInternetException
import com.khush.newsapp.common.dispatcher.DispatcherProvider
import com.khush.newsapp.common.networkhelper.NetworkHelper
import com.khush.newsapp.data.model.Country
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
class CountryFilterViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val networkHelper: NetworkHelper
) :
    ViewModel() {


    private val _countryItem = MutableStateFlow<UIState<List<Country>>>(UIState.Empty)
    val countryItem: StateFlow<UIState<List<Country>>> = _countryItem

    init {
        getCountries()
    }

    fun getCountries() {
        viewModelScope.launch {
            if (!networkHelper.isNetworkConnected()) {
                _countryItem.emit(
                    UIState.Failure(
                        throwable = NoInternetException()
                    )
                )
                return@launch
            }
            _countryItem.emit(UIState.Loading)
            newsRepository.getCountries()
                .flowOn(dispatcherProvider.io)
                .catch {
                    _countryItem.emit(UIState.Failure(it))
                }
                .collect {
                    _countryItem.emit(UIState.Success(it))
                }
        }
    }
}