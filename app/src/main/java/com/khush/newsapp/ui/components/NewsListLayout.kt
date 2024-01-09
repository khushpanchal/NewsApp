package com.khush.newsapp.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import com.khush.newsapp.data.database.entity.Article


@Composable
fun NewsLayout(
    newsList: List<Article>,
    articleClicked: (Article) -> Unit
) {
    LazyColumn {
        items(newsList) {
            Article(it) { article ->
                articleClicked(article)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsLayoutWithDelete(
    newsList: List<Article>,
    articleClicked: (Article) -> Unit,
    deleteArticle: (Article) -> Unit
) {
    LazyColumn {
        items(newsList, key = { article -> article.url!! }) { article ->
            val dismissState = rememberDismissState()
            if (dismissState.isDismissed(direction = DismissDirection.EndToStart) || dismissState.isDismissed(
                    direction = DismissDirection.StartToEnd
                )
            ) {
                deleteArticle(article)
            }
            SwipeToDismiss(state = dismissState,
                background = {},
                dismissContent = {
                    Article(article) {
                        articleClicked(it)
                    }
                })

        }
    }
}