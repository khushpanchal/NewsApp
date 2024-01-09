package com.khush.newsapp.ui.base

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.khush.newsapp.R
import com.khush.newsapp.common.util.NavigationUtil.navigateSingleTopTo
import com.khush.newsapp.common.util.NavigationUtil.navigateToCountryScreen
import com.khush.newsapp.common.util.NavigationUtil.navigateToLanguageScreen
import com.khush.newsapp.common.util.NavigationUtil.navigateToSourceScreen
import com.khush.newsapp.common.util.ValidationUtil.checkIfValidArgNews
import com.khush.newsapp.data.database.entity.Article
import com.khush.newsapp.ui.screens.ArticleScreen
import com.khush.newsapp.ui.screens.CountryScreen
import com.khush.newsapp.ui.screens.LanguageScreen
import com.khush.newsapp.ui.screens.NewsScreen
import com.khush.newsapp.ui.screens.NewsScreenPaging
import com.khush.newsapp.ui.screens.SavedScreen
import com.khush.newsapp.ui.screens.SearchScreen
import com.khush.newsapp.ui.screens.SourceScreen
import java.net.URLEncoder
import kotlin.text.Charsets.UTF_8


@Composable
fun NewsNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentScreen =
        bottomBarScreens.find { it.route == currentDestination?.route } ?: Route.TopNews

    Scaffold(
        topBar = {
            NewsTopBar {
                if (navController.currentBackStackEntry?.destination?.route == Route.NewsArticle.route
                    || navController.currentBackStackEntry?.destination?.route == Route.FilterNews.route
                    || navController.currentBackStackEntry?.destination?.route == Route.TopNews.route
                ) {
                    navController.popBackStack()
                } else {
                    navigateSingleTopTo(Route.TopNews.route, navController)
                }
            }
        },
        bottomBar = {
            NewsBottomNavigation(
                currentScreen = currentScreen
            ) {
                navigateSingleTopTo(it.route, navController)
            }
        }
    ) {
        NewsNavHost(
            modifier = Modifier.padding(it),
            navController = navController
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsTopBar(onBackClicked: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(stringResource(id = R.string.app_name))
        },
        navigationIcon = {
            IconButton(onClick = { onBackClicked() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
private fun NewsNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Route.TopNews.route,
        modifier = modifier
    ) {
        composable(
            route = Route.TopNews.route,
            arguments = listOf(navArgument("country") { type = NavType.StringType },
                navArgument("language") { type = NavType.StringType },
                navArgument("source") { type = NavType.StringType })
        ) {
            val countryCode = it.arguments?.getString("country")
            val languageCode = it.arguments?.getString("language")
            val sourceCode = it.arguments?.getString("source")

            if (checkIfValidArgNews(countryCode) || checkIfValidArgNews(languageCode) || checkIfValidArgNews(
                    sourceCode
                )
            ) {
                NewsScreen { article ->
                    navigateToArticleScreen(article, navController)
                }
            } else {
                NewsScreenPaging { article ->
                    navigateToArticleScreen(article, navController)
                }
            }
        }
        composable(route = Route.FilterNews.route) {
            FilterNavHost(parentNavController = navController)
        }
        composable(route = Route.SavedNews.route) {
            SavedScreen {
                navigateToArticleScreen(it, navController)
            }
        }
        composable(route = Route.SearchNews.route) {
            SearchScreen(
                backPressed = {
                    navigateSingleTopTo(Route.TopNews.route, navController)
                }
            ) {
                navigateToArticleScreen(it, navController)
            }
        }
        composable(
            route = Route.NewsArticle.route,
            arguments = listOf(navArgument("article") { type = NavType.StringType })
        ) {
            val articleJson = it.arguments?.getString("article")
            val gson = Gson()
            val article = gson.fromJson(articleJson, Article::class.java)
            ArticleScreen(
                article = article
            )
        }
    }
}


@Composable
fun NewsBottomNavigation(
    currentScreen: Route,
    onIconSelected: (Route) -> Unit
) {
    NavigationBar {
        bottomBarScreens.forEach { screen ->
            NavigationBarItem(
                selected = screen == currentScreen,
                label = {
                    Text(text = stringResource(id = screen.resourceId))
                },
                icon = {
                    Icon(painter = painterResource(id = screen.icon), null)
                },
                onClick = {
                    onIconSelected.invoke(screen)
                }
            )
        }
    }
}

private fun navigateToArticleScreen(
    it: Article,
    navController: NavHostController
) {
    val articleJsonString = Gson().toJson(it)
    val encodedArticle = URLEncoder.encode(articleJsonString, UTF_8.name())
    navController.navigate(Route.NewsArticle.routeWithoutArgs + "/${encodedArticle}") {
        launchSingleTop = true
    }
}

@Composable
fun FilterNavHost(
    parentNavController: NavHostController
) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentScreen =
        filterScreens.find { it.route == currentDestination?.route } ?: FilterRoute.Country

    Scaffold(
        topBar = {
            FilterNavigation(
                currentScreen = currentScreen
            ) {
                navigateSingleTopTo(it.route, navController)
            }
        }
    ) {
        FilterNavHost(
            navController = navController,
            parentNavController = parentNavController,
            modifier = Modifier.padding(it)
        )
    }

}

@Composable
fun FilterNavHost(
    navController: NavHostController,
    parentNavController: NavHostController,
    modifier: Modifier = Modifier
) {
    val mContext = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = FilterRoute.Country.route,
        modifier = modifier
    ) {
        composable(route = FilterRoute.Country.route) {
            CountryScreen {
                navigateToCountryScreen(it.code, parentNavController)
            }
        }
        composable(route = FilterRoute.Language.route) {
            LanguageScreen {
                navigateToLanguageScreen(it.code, parentNavController)
            }
        }
        composable(route = FilterRoute.Source.route) {
            SourceScreen {
                if (it.id != null) {
                    navigateToSourceScreen(it.id, parentNavController)
                } else {
                    Toast.makeText(
                        mContext,
                        mContext.resources.getString(R.string.news_not_available),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

@Composable
fun FilterNavigation(
    currentScreen: FilterRoute,
    onIconSelected: (FilterRoute) -> Unit
) {
    NavigationBar {
        filterScreens.forEach { screen ->
            NavigationBarItem(
                selected = screen == currentScreen,
                label = {
                    Text(text = stringResource(id = screen.resourceId))
                },
                icon = {
                    Icon(painter = painterResource(id = screen.icon), null)
                },
                onClick = {
                    onIconSelected.invoke(screen)
                }
            )
        }
    }
}




