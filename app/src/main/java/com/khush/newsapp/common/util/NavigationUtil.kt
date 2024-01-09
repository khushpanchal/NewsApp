package com.khush.newsapp.common.util

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.khush.newsapp.ui.base.Route

object NavigationUtil {

    fun navigateSingleTopTo(
        route: String,
        navController: NavHostController
    ) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id)
            launchSingleTop = true
        }
    }

    fun navigateToCountryScreen(
        countryId: String,
        navController: NavHostController
    ) {
        navController.navigate(Route.TopNews.routeWithoutArgs + "/${countryId}/{language}/{source}") {
            launchSingleTop = true
        }
    }

    fun navigateToLanguageScreen(
        languageId: String,
        navController: NavHostController
    ) {
        navController.navigate(Route.TopNews.routeWithoutArgs + "/{country}/${languageId}/{source}") {
            launchSingleTop = true
        }
    }

    fun navigateToSourceScreen(
        sourceId: String,
        navController: NavHostController
    ) {
        navController.navigate(Route.TopNews.routeWithoutArgs + "/{country}/{language}/${sourceId}") {
            launchSingleTop = true
        }
    }

}