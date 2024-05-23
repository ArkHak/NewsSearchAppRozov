package com.example.news_data

import com.example.news.database.NewsDatabase
import com.example.news.database.models.ArticleDBO
import com.example.news_data.models.Article
import com.example.newsapi.NewsApi
import com.example.newsapi.models.ArticleDTO
import com.example.newsapi.models.ResponseDTO
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

class ArticlesRepository @Inject constructor(
    private val database: NewsDatabase,
    private val api: NewsApi,
) {

    fun getAll(
        mergeStrategy: MergeStrategy<RequestResult<List<Article>>> = RequestResponseMergeStrategy()
    ): Flow<RequestResult<List<Article>>> {

        val cachedAllArticles: Flow<RequestResult<List<Article>>> = getAllFromDatabase()
        val remoteArticles: Flow<RequestResult<List<Article>>> = getAllFromServer()

        return cachedAllArticles.combine(remoteArticles, mergeStrategy::merge)
            .flatMapConcat { result ->
                if (result is RequestResult.Success) {
                    database.articlesDao.observeAll()
                        .map { dbos -> dbos.map { it.toArticle() } }
                        .map { RequestResult.Success(it) }
                } else {
                    flowOf(result)
                }
            }

    }

    private fun getAllFromDatabase(): Flow<RequestResult<List<Article>>> {
        val dbRequest = database.articlesDao::getAll.asFlow()
            .map { RequestResult.Success(it) }
        val start = flowOf<RequestResult<List<ArticleDBO>>>(RequestResult.InProgress())
        return merge(start, dbRequest).map { result ->
            result.map { articlesDbos ->
                articlesDbos.map { it.toArticle() }
            }
        }
    }

    private fun getAllFromServer(): Flow<RequestResult<List<Article>>> {
        val apiRequest = flow { emit(api.everything()) }
            .onEach { result ->
                if (result.isSuccess) {
                    saveResponseToCache(result.getOrThrow().articles)
                }
            }
            .map { it.toRequestResult() }

        val start = flowOf<RequestResult<ResponseDTO<ArticleDTO>>>(RequestResult.InProgress())

        return merge(apiRequest, start)
            .map { result ->
                result.map { response ->
                    response.articles.map { it.toArticle() }
                }
            }
    }

    private suspend fun saveResponseToCache(data: List<ArticleDTO>) {
        val dbos = data.map { articleDto -> articleDto.toArticleDbo() }
        database.articlesDao.insert(dbos)
    }

    suspend fun search(query: String): Flow<Article> {
        api.everything()
        TODO("Not implemented")
    }

}