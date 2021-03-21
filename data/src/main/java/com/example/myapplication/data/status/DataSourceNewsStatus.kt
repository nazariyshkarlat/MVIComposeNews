package com.example.myapplication.data.status

import com.example.myapplication.data.entity.ArticleEntity
import com.example.myapplication.data.entity.toDomain
import com.example.myapplication.domain.status.LoadingStatus
import com.example.myapplication.domain.status.NewsStatus
import com.example.myapplication.domain.status.pageNumber

sealed class DataSourceNewsStatus {
    data class Success(val articles: List<ArticleEntity>) : DataSourceNewsStatus()
    object NetworkUnavailable : DataSourceNewsStatus()
    object ServerUnavailable : DataSourceNewsStatus()
    object Error : DataSourceNewsStatus()
}

@ExperimentalStdlibApi
fun DataSourceNewsStatus.toDomain(currentState: NewsStatus, isNewPage: Boolean) =
    if(currentState is NewsStatus.SuccessWithResult) {
        when (this) {
            DataSourceNewsStatus.Error -> {
                if(isNewPage) currentState.copy(paginationLoadingStatus = LoadingStatus.LOADING_ERROR)
                else currentState.copy(refreshLoadingStatus = LoadingStatus.LOADING_ERROR)
            }
            DataSourceNewsStatus.NetworkUnavailable -> {
                if(isNewPage) currentState.copy(paginationLoadingStatus = LoadingStatus.LOADING_ERROR)
                else currentState.copy(refreshLoadingStatus = LoadingStatus.LOADING_ERROR)
            }
            DataSourceNewsStatus.ServerUnavailable -> {
                if(isNewPage) currentState.copy(paginationLoadingStatus = LoadingStatus.LOADING_ERROR)
                else currentState.copy(refreshLoadingStatus = LoadingStatus.LOADING_ERROR)
            }
            is DataSourceNewsStatus.Success -> {
                NewsStatus.SuccessWithResult(buildList {
                    if(isNewPage) {
                        addAll(currentState.articles)
                        addAll(this@toDomain.articles.map { it.toDomain() })
                    }else{
                        addAll(this@toDomain.articles.map { it.toDomain() }.filter { !currentState.articles.contains(it) })
                        addAll(currentState.articles)
                    }
                }, paginationLoadingStatus = if(this.articles.isEmpty()) LoadingStatus.NO_LOADING else LoadingStatus.LOADING,
                    refreshLoadingStatus = LoadingStatus.NO_LOADING,
                    pageNumber = if(this.articles.isEmpty()) currentState.pageNumber else currentState.pageNumber+1)
            }
        }
    } else{
        when (this) {
            DataSourceNewsStatus.Error -> NewsStatus.Error
            DataSourceNewsStatus.NetworkUnavailable -> NewsStatus.NetworkUnavailable
            DataSourceNewsStatus.ServerUnavailable -> NewsStatus.ServerUnavailable
            is DataSourceNewsStatus.Success -> {
                if (this.articles.isEmpty()) NewsStatus.EmptyResult
                else NewsStatus.SuccessWithResult(this.articles.map { it.toDomain() },
                    paginationLoadingStatus = LoadingStatus.LOADING,
                    refreshLoadingStatus = LoadingStatus.NO_LOADING,
                    pageNumber = currentState.pageNumber+1)
            }
        }
    }