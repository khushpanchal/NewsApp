package com.khush.newsapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.khush.newsapp.R
import com.khush.newsapp.data.database.entity.Article
import com.khush.newsapp.ui.base.ShowError
import com.khush.newsapp.ui.base.WebViewPage
import com.khush.newsapp.ui.viewmodels.SharedViewModel

@Composable
fun ArticleScreen(
    article: Article?,
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    val mContext = LocalContext.current

    Scaffold(
        floatingActionButton = {

            FloatingActionButton(onClick = {
                if (article != null) {
                    sharedViewModel.saveArticle(article)
                }
                Toast.makeText(
                    mContext,
                    mContext.resources.getString(R.string.article_saved_successfully),
                    Toast.LENGTH_SHORT
                ).show()
            }) {
                Icon(painter = painterResource(id = R.drawable.ic_save), contentDescription = null)
            }
        }
    ) {
        if (article?.url == null) {
            ShowError(text = stringResource(id = R.string.something_went_wrong))
        } else {
            WebViewPage(
                url = article.url,
                modifier = Modifier.padding(it)
            )
        }
    }
}