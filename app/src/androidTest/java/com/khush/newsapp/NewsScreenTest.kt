package com.khush.newsapp

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import com.khush.newsapp.data.database.entity.Article
import com.khush.newsapp.data.database.entity.Source
import com.khush.newsapp.ui.base.ShowError
import com.khush.newsapp.ui.base.ShowLoading
import com.khush.newsapp.ui.components.NewsLayout
import org.junit.Rule
import org.junit.Test


class NewsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loading_whenShowLoading_isShown() {
        composeTestRule.setContent {
            ShowLoading()
        }

        composeTestRule
            .onNodeWithContentDescription(composeTestRule.activity.resources.getString(R.string.loading))
            .assertExists()
    }

    @Test
    fun articles_whenNewsLayout_isShown() {
        composeTestRule.setContent {
            NewsLayout(
                newsList = testArticles,
                articleClicked = {}
            )
        }

        composeTestRule
            .onNodeWithText(
                testArticles[0].title!!,
                substring = true
            )
            .assertExists()
            .assertHasClickAction()

        composeTestRule.onNode(hasScrollToNodeAction())
            .performScrollToNode(
                hasText(
                    testArticles[1].title!!,
                    substring = true
                )
            )

        composeTestRule
            .onNodeWithText(
                testArticles[2].title!!,
                substring = true
            )
            .assertExists()
            .assertHasClickAction()
    }

    @Test
    fun error_whenShowError_isShown() {
        val errorMessage = "Error Message For You"

        composeTestRule.setContent {
            ShowError(
                text = errorMessage
            )
        }

        composeTestRule
            .onNodeWithText(errorMessage)
            .assertExists()
    }

}

private val testArticles = listOf(
    Article(
        id = 1,
        title = "title1",
        description = "description1",
        url = "url1",
        urlToImage = "imageUrl1",
        content = "content1",
        publishedAt = "publishAt1",
        author = "author1",
        source = Source(id = "sourceId1", name = "sourceName1")
    ),
    Article(
        id = 2,
        title = "title2",
        description = "description2",
        url = "url2",
        urlToImage = "imageUrl2",
        content = "content2",
        publishedAt = "publishAt2",
        author = "author2",
        source = Source(id = "sourceId2", name = "sourceName2")
    ),
    Article(
        id = 3,
        title = "title3",
        description = "description3",
        url = "url3",
        urlToImage = "imageUrl3",
        content = "content3",
        publishedAt = "publishAt3",
        author = "author3",
        source = Source(id = "sourceId3", name = "sourceName3")
    ),
    Article(
        id = 4,
        title = "title4",
        description = "description4",
        url = "url4",
        urlToImage = "imageUrl4",
        content = "content4",
        publishedAt = "publishAt4",
        author = "author4",
        source = Source(id = "sourceId4", name = "sourceName4")
    ),
)