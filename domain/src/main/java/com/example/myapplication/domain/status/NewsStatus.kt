package com.example.myapplication.domain.status

import com.example.myapplication.domain.entity.Article
import kotlinx.serialization.Serializable

@Serializable
sealed class NewsStatus {
    @Serializable
    data class SuccessWithResult(val articles: List<Article>,
                                 val paginationLoadingStatus: LoadingStatus,
                                 val refreshLoadingStatus: LoadingStatus,
                                 val pageNumber: Int) : NewsStatus()
    @Serializable
    object Loading : NewsStatus()
    @Serializable
    object EmptyResult : NewsStatus()
    @Serializable
    object NetworkUnavailable : NewsStatus()
    @Serializable
    object ServerUnavailable : NewsStatus()
    @Serializable
    object Error : NewsStatus()
}

enum class LoadingStatus{
    NO_LOADING,
    LOADING_ERROR,
    LOADING
}

val NewsStatus.pageNumber
 get() = if(this is NewsStatus.SuccessWithResult) this.pageNumber else 1