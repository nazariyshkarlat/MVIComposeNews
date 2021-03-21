package com.example.myapplication.data.repository

import com.example.myapplication.data.repository.data_source.NewsDataSourceFactory
import com.example.myapplication.data.status.toDomain
import com.example.myapplication.domain.NewsRepository
import com.example.myapplication.domain.status.LoadingStatus
import com.example.myapplication.domain.status.NewsStatus
import com.example.myapplication.domain.status.pageNumber
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsRepositoryImpl(private val newsDataSourceFactory: NewsDataSourceFactory
) : NewsRepository{
    override val pageSize: Int = 20
    override val country: String = "de"
    override val category: String = "science"
    override var currentState: NewsStatus = NewsStatus.Loading

    @ExperimentalStdlibApi
    override fun getNews(isNewPage: Boolean) = flow {
            if (currentState.pageNumber == 0) {
                NewsStatus.Loading.let {
                    currentState = it
                    emit(it)
                }
            }
        if(!isNewPage && currentState is NewsStatus.SuccessWithResult){
            (currentState as NewsStatus.SuccessWithResult).copy(refreshLoadingStatus = LoadingStatus.LOADING).let {
                currentState = it
                emit(it)
            }
        }
        newsDataSourceFactory.create(priority = NewsDataSourceFactory.Priority.CLOUD)
            .getNews(if(isNewPage) currentState.pageNumber+1 else 1, pageSize, country, category)
            .toDomain(currentState, isNewPage).let {
                currentState = it
                emit(it)
            }
        }

    override fun restoreNews() = flow{
        emit(currentState)
    }
}