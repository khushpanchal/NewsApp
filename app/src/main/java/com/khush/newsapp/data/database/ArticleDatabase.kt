package com.khush.newsapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.khush.newsapp.data.database.dao.SavedArticleDao
import com.khush.newsapp.data.database.entity.SavedArticleEntity

@Database(entities = [SavedArticleEntity::class], version = 1, exportSchema = false)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getSavedArticleDao(): SavedArticleDao

}