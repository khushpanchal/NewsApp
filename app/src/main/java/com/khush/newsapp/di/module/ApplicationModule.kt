package com.khush.newsapp.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.khush.newsapp.common.Const
import com.khush.newsapp.common.dispatcher.DefaultDispatcherProvider
import com.khush.newsapp.common.dispatcher.DispatcherProvider
import com.khush.newsapp.common.logger.AppLogger
import com.khush.newsapp.common.logger.Logger
import com.khush.newsapp.common.networkhelper.NetworkHelper
import com.khush.newsapp.common.networkhelper.NetworkHelperImpl
import com.khush.newsapp.data.database.AppDatabaseService
import com.khush.newsapp.data.database.ArticleDatabase
import com.khush.news.data.database.DatabaseService
import com.khush.newsapp.data.network.ApiInterface
import com.khush.newsapp.di.ApiKey
import com.khush.newsapp.di.BaseUrl
import com.khush.newsapp.di.DbName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Singleton
    @Provides
    fun provideArticleDatabase(
        application: Application,
        @DbName dbName: String
    ): ArticleDatabase {
        return Room.databaseBuilder(
            application,
            ArticleDatabase::class.java,
            dbName
        )
            .build()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @ApiKey
    @Provides
    fun provideApiKey(): String = Const.API_KEY

    @BaseUrl
    @Provides
    fun provideBaseUrl(): String = Const.BASE_URL

    @DbName
    @Provides
    fun provideDbName(): String = Const.DB_NAME

    @Singleton
    @Provides
    fun provideNetworkService(
        @BaseUrl baseUrl: String,
        gsonFactory: GsonConverterFactory,
    ): ApiInterface {
        val client = OkHttpClient
            .Builder()
            .build()

        return Retrofit
            .Builder()
            .client(client) //adding client to intercept all responses
            .baseUrl(baseUrl)
            .addConverterFactory(gsonFactory)
            .build()
            .create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideLogger(): Logger = AppLogger()

    @Provides
    @Singleton
    fun provideDispatcher(): DispatcherProvider = DefaultDispatcherProvider()

    @Provides
    @Singleton
    fun provideNetworkHelper(
        @ApplicationContext context: Context
    ): NetworkHelper {
        return NetworkHelperImpl(context)
    }

    @Provides
    @Singleton
    fun provideDatabaseService(articleDatabase: ArticleDatabase): DatabaseService {
        return AppDatabaseService(articleDatabase)
    }

}