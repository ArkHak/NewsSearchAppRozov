package com.example.news_main

import com.example.news_data.ArticlesRepository
import com.example.news_data.models.Article
import kotlinx.coroutines.flow.Flow

class GetAllArticlesUseCase(
    private val repository: ArticlesRepository
) {

    operator fun invoke(): Flow<Article> {
        return repository.getAll()
    }
}