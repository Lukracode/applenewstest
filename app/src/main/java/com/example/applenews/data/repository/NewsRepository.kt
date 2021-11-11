package com.example.applenews.data.repository

import com.example.applenews.data.api.RetrofitInstance
import com.example.applenews.data.database.ArticleDatabase
import com.example.applenews.models.Article

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getTopNews(topicKey: String, pageNumber: Int) =
        RetrofitInstance.api.getTopNews(topicKey, pageNumber)

    suspend fun getAllNews(topicKey: String, pageNumber: Int) =
        RetrofitInstance.api.getAllNews(topicKey, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}