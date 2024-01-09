package com.khush.newsapp.ui.base

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.khush.newsapp.R

sealed class Route(
    val route: String,
    @StringRes val resourceId: Int,
    @DrawableRes val icon: Int,
    val routeWithoutArgs: String = route
) {
    object TopNews :
        Route("topNews/{country}/{language}/{source}", R.string.news, R.drawable.ic_news, "topNews")

    object FilterNews : Route("filterNews", R.string.filter, R.drawable.ic_filter)
    object SavedNews : Route("savedNews", R.string.saved, R.drawable.ic_save)
    object SearchNews : Route("searchNews", R.string.search, R.drawable.ic_search)
    object NewsArticle :
        Route("newsArticle/{article}", R.string.news, R.drawable.ic_news, "newsArticle")
}

sealed class FilterRoute(
    val route: String,
    @StringRes val resourceId: Int,
    @DrawableRes val icon: Int
) {
    object Country : FilterRoute("countryFilter", R.string.country, R.drawable.ic_country)
    object Language : FilterRoute("languageFilter", R.string.language, R.drawable.ic_language)
    object Source : FilterRoute("sourceFilter", R.string.source, R.drawable.ic_source)
}

val bottomBarScreens = listOf(
    Route.TopNews,
    Route.FilterNews,
    Route.SavedNews,
    Route.SearchNews
)

val filterScreens = listOf(
    FilterRoute.Country,
    FilterRoute.Language,
    FilterRoute.Source
)