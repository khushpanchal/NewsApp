package com.khush.newsapp.data.database.entity

data class Article(

    var id: Int = 0,

    val source: Source,

    val author: String?,

    val title: String?,

    val description: String?,

    val url: String?,

    val urlToImage: String?,

    val publishedAt: String?,

    val content: String?

)