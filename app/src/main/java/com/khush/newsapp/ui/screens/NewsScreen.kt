package com.khush.newsapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khush.newsapp.R
import com.khush.newsapp.common.NoInternetException
import com.khush.newsapp.common.util.filterArticles
import com.khush.newsapp.data.database.entity.Article
import com.khush.newsapp.ui.base.ShowError
import com.khush.newsapp.ui.base.ShowLoading
import com.khush.newsapp.ui.base.UIState
import com.khush.newsapp.ui.components.NewsLayout
import com.khush.newsapp.ui.viewmodels.NewsViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewsScreen(
    newsViewModel: NewsViewModel = hiltViewModel(),
    newsClicked: (Article) -> Unit
) {
    val newsUiState: UIState<List<Article>> by newsViewModel.newsItem.collectAsStateWithLifecycle()

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            newsViewModel.fetchNews()
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        when (newsUiState) {
            is UIState.Loading -> {
                if (!isRefreshing)
                    ShowLoading()
            }

            is UIState.Failure -> {
                isRefreshing = false
                var errorText = stringResource(id = R.string.something_went_wrong)
                if ((newsUiState as UIState.Failure<List<Article>>).throwable is NoInternetException) {
                    errorText = stringResource(id = R.string.no_internet_available)
                }
                ShowError(
                    text = errorText,
                    retryEnabled = true
                ) {
                    newsViewModel.fetchNews()
                }
            }

            is UIState.Success -> {
                isRefreshing = false
                if ((newsUiState as UIState.Success<List<Article>>).data.filterArticles()
                        .isEmpty()
                ) {
                    ShowError(text = stringResource(R.string.no_data_available))
                } else {
                    NewsLayout(newsList = (newsUiState as UIState.Success<List<Article>>).data.filterArticles()) {
                        newsClicked(it)
                    }
                }
            }

            is UIState.Empty -> {

            }
        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
