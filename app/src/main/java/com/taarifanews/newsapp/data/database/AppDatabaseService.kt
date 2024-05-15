package com.taarifanews.newsapp.data.database

import com.taarifanews.newsapp.common.util.articleToSavedArticleEntity
import com.taarifanews.newsapp.common.util.savedArticleEntityToArticle
import com.taarifanews.newsapp.data.database.entity.Article
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow

class AppDatabaseService(
    private val articleDatabase: ArticleDatabase
) : DatabaseService {
    override suspend fun upsert(article: Article) {
        articleDatabase.getSavedArticleDao().upsert(article.articleToSavedArticleEntity())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getSavedArticles(): Flow<List<Article>> {
        return articleDatabase.getSavedArticleDao().getSavedArticles().flatMapConcat {
            flow {
                val list = mutableListOf<Article>()
                it.forEach { savedArticleEntity ->
                    list.add(savedArticleEntity.savedArticleEntityToArticle())
                }
                emit(list)
            }
        }
    }

    override suspend fun deleteArticle(article: Article) {
        articleDatabase.getSavedArticleDao().deleteArticle(article.articleToSavedArticleEntity())
    }

    override fun getAllArticles(): Flow<List<Article>> {
        return articleDatabase.getArticleDao().getAllArticles()
    }

    override fun deleteAllAndInsertAll(articles: List<Article>) {
        articleDatabase.getArticleDao().deleteAllAndInsertAll(articles)
    }


}