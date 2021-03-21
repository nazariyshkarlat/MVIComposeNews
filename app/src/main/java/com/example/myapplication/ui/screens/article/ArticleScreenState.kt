package com.example.myapplication.ui.screens.article

import com.example.myapplication.domain.entity.Article
import com.example.myapplication.domain.status.LoadingStatus
import com.example.myapplication.domain.status.NewsStatus
import com.example.myapplication.ui.screens.ArticleUiModel
import com.example.myapplication.ui.screens.toUiModel

sealed class ArticleScreenState {
    object LoadingScreen: ArticleScreenState()
    data class ScreenWithData(val article: ArticleUiModel) : ArticleScreenState()
    data class ErrorScreen(val errorCase: ErrorCase) : ArticleScreenState()
}

enum class ErrorCase{
    SERVER_UNAVAILABLE,
    NETWORK_UNAVAILABLE,
    ERROR
}

fun ArticleUiModel.toArticleScreenState() = ArticleScreenState.ScreenWithData(article = this)

