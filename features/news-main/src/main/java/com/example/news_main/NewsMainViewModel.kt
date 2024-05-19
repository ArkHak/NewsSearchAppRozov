package com.example.news_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news_data.RequestResult
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class NewsMainViewModel(
    private val getAllArticlesUseCase: GetAllArticlesUseCase
) : ViewModel() {

    val state: StateFlow<State> = getAllArticlesUseCase()
        .map { it.toState() }
        .stateIn(viewModelScope, SharingStarted.Lazily, State.None)
}

private fun RequestResult<List<Article>>.toState(): State {
    return when (this) {
        is RequestResult.Error -> State.Error()
        is RequestResult.InProgress -> State.Loading(data)
        is RequestResult.Success -> State.Success(checkNotNull(data))
    }
}

sealed class State {
    object None : State()
    class Loading(val articles: List<Article>?) : State()
    class Error : State()
    class Success(val articles: List<Article>) : State()
}