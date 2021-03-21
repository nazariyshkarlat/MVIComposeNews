package com.example.myapplication.ui.screens.news_list

import com.example.myapplication.domain.status.LoadingStatus
import com.example.myapplication.domain.status.NewsStatus
import com.example.myapplication.ui.screens.ArticleUiModel
import com.example.myapplication.ui.screens.toUiModel

sealed class NewsScreenState {
    object LoadingScreen: NewsScreenState()
    object EmptyScreen : NewsScreenState()
    data class ScreenWithData(val articles: List<ArticleUiModel>,
                         val refreshLoadingStatus: LoadingStatus,
                         val paginationLoadingStatus: LoadingStatus) : NewsScreenState()
    data class ErrorScreen(val errorCase: ErrorCase) : NewsScreenState()

    fun isCanLoadNewPage() = this is ScreenWithData && this.paginationLoadingStatus == LoadingStatus.LOADING
}

enum class ErrorCase{
    SERVER_UNAVAILABLE,
    NETWORK_UNAVAILABLE,
    ERROR
}

fun NewsStatus.toUiNewsScreenState() =
    when(this){
        NewsStatus.EmptyResult -> NewsScreenState.EmptyScreen
        NewsStatus.Error -> NewsScreenState.ErrorScreen(ErrorCase.ERROR)
        NewsStatus.Loading -> NewsScreenState.LoadingScreen
        NewsStatus.NetworkUnavailable -> NewsScreenState.ErrorScreen(ErrorCase.NETWORK_UNAVAILABLE)
        NewsStatus.ServerUnavailable -> NewsScreenState.ErrorScreen(ErrorCase.SERVER_UNAVAILABLE)
        is NewsStatus.SuccessWithResult -> NewsScreenState.ScreenWithData(articles.map { it.toUiModel() }, refreshLoadingStatus, paginationLoadingStatus)
    }