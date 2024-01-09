package com.khush.newsapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khush.newsapp.R
import com.khush.newsapp.data.database.entity.Article
import com.khush.newsapp.ui.base.ShowError
import com.khush.newsapp.ui.components.NewsLayoutWithDelete
import com.khush.newsapp.ui.viewmodels.SharedViewModel

@Composable
fun SavedScreen(
    sharedViewModel: SharedViewModel = hiltViewModel(),
    newsClicked: (Article) -> Unit
) {

    val newsList: List<Article> by sharedViewModel.getSavedNews()
        .collectAsStateWithLifecycle(emptyList())

    if (newsList.isEmpty()) {
        ShowError(text = stringResource(R.string.no_saved_news))
    } else {
        NewsLayoutWithDelete(newsList = newsList,
            articleClicked = {
                newsClicked(it)
            }) {
            sharedViewModel.deleteArticle(it)
        }
    }

}