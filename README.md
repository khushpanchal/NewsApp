# NewsApp

Welcome to NewsApp, a news application built with MVVM architecture and Jetpack Compose.

## Major Highlights

- **Jetpack Compose** for modern UI
- **Offline caching** with a **single source of truth**
- **MVVM architecture** for a clean and scalable codebase
- **Kotlin** and **Kotlin DSL**
- **Dagger Hilt** for efficient dependency injection.
- **Retrofit** for seamless networking
- **Room DB** for local storage of news articles
- **Coroutines** and **Flow** for asynchronous programming
- **StateFlow** for streamlined state management
- **Pagination** to efficiently load and display news articles
- **Unit tests** and **UI tests** for robust code coverage
- **Instant search** for quick access to relevant news
- **Navigation** for smooth transitions between screens
- **WebView** for a seamless reading experience
- **WorkManager** for periodic news fetching
- **Notification** for alerting about latest news
- **Coil** for efficient image loading
- Pull to refresh for refreshing news content
- Swipe to delete for managing saved news articles

<p align="center">
<img alt="mvvm-architecture"  src="https://github.com/khushpanchal/NewsApp/blob/master/assets/News_app_architecture.jpeg">
</p>

## Features Implemented

- Show top news articles
- Filter news by country, language, and source
- Save news articles for future reference
- Search for specific news articles
- View news articles in a WebView for a detailed reading experience

## Dependency Use

- Jetpack Compose for UI: Modern UI toolkit for building native Android UIs
- Coil for Image Loading: Efficiently loads and caches images
- Retrofit for Networking: A type-safe HTTP client for smooth network requests
- Dagger Hilt for Dependency Injection: Simplifies dependency injection
- Room for Database: A SQLite object mapping library for local data storage
- Paging Compose for Pagination: Simplifies the implementation of paginated lists
- Mockito, JUnit, Turbine for Testing: Ensures the reliability of the application

## How to Run the Project

- Clone the Repository:
```
git clone https://github.com/khushpanchal/NewsApp.git
cd NewsApp
```
- Visit newsapi.org and sign up for an API key, Copy the API key provided
- Open the build.gradle.kts file in the app module. Find the following line
```
buildConfigField("String", "API_KEY", "\"<Add your API Key>\"")
```
- Replace "Add your API Key" with the API key you obtained
- Build and run the NewsApp.


## The Complete Project Folder Structure

```
|── NewsApplication.kt
├── common
│   ├── Const.kt
│   ├── NoInternetException.kt
│   ├── dispatcher
│   │   ├── DefaultDispatcherProvider.kt
│   │   └── DispatcherProvider.kt
│   ├── logger
│   │   ├── AppLogger.kt
│   │   └── Logger.kt
│   ├── networkhelper
│   │   ├── NetworkHelper.kt
│   │   └── NetworkHelperImpl.kt
│   └── util
│       ├── EntityUtil.kt
│       ├── NavigationUtil.kt
│       ├── TimeUtil.kt
│       ├── Util.kt
│       └── ValidationUtil.kt
├── data
│   ├── database
│   │   ├── AppDatabaseService.kt
│   │   ├── ArticleDatabase.kt
│   │   ├── DatabaseService.kt
│   │   ├── dao
│   │   │   ├── ArticleDao.kt
│   │   │   └── SavedArticleDao.kt
│   │   └── entity
│   │       ├── Article.kt
│   │       ├── SavedArticleEntity.kt
│   │       ├── SavedSourceEntity.kt
│   │       └── Source.kt
│   ├── model
│   │   ├── ApiArticle.kt
│   │   ├── ApiSource.kt
│   │   ├── Country.kt
│   │   ├── Language.kt
│   │   ├── News.kt
│   │   └── Sources.kt
│   ├── network
│   │   ├── ApiInterface.kt
│   │   └── ApiKeyInterceptor.kt
│   └── repository
│       └── NewsRepository.kt
├── di
│   ├── module
│   │   └── ApplicationModule.kt
│   └── qualifiers.kt
├── ui
│   ├── NewsActivity.kt
│   ├── base
│   │   ├── CommonUI.kt
│   │   ├── NewsDestination.kt
│   │   ├── NewsNavigation.kt
│   │   └── UIState.kt
│   ├── components
│   │   ├── Article.kt
│   │   ├── FilterItem.kt
│   │   ├── FilterItemListLayout.kt
│   │   └── NewsListLayout.kt
│   ├── paging
│   │   └── NewsPagingSource.kt
│   ├── screens
│   │   ├── ArticleScreen.kt
│   │   ├── FilterScreen.kt
│   │   ├── NewsScreen.kt
│   │   ├── SavedScreen.kt
│   │   └── SearchScreen.kt
│   ├── theme
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   └── viewmodels
│       ├── NewsViewModel.kt
│       ├── SearchViewModel.kt
│       ├── SharedViewModel.kt
│       └── filters
│           ├── CountryFilterViewModel.kt
│           ├── LanguageFilterViewModel.kt
│           └── SourceFilterViewModel.kt
└── worker
    └── NewsWorker.kt
```

<p align="center">
<img alt="screenshots"  src="https://github.com/khushpanchal/NewsApp/blob/master/assets/News_app.jpeg">
</p>

## If this project helps you, show love ❤️ by putting a ⭐ on this project ✌️

## Contribute to the project

Feel free to improve or add features to the project.
Create an issue or find the pending issue. All pull requests are welcome 😄

