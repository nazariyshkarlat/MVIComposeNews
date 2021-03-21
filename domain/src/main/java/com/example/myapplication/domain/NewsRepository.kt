package com.example.myapplication.domain

import com.example.myapplication.domain.entity.Article
import com.example.myapplication.domain.status.NewsStatus
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    val pageSize: Int
    val country: String
    val category: String
    var currentState: NewsStatus

    fun getNews(isNewPage: Boolean) : Flow<NewsStatus>

    fun restoreNews() : Flow<NewsStatus>
}