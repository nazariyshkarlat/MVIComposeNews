package com.example.myapplication.domain.interactor

import com.example.myapplication.domain.NewsRepository
import com.example.myapplication.domain.status.NewsStatus
import kotlinx.coroutines.flow.Flow

interface NewsInteractor {
    companion object{
        const val CURRENT_STATE ="CURRENT_STATE"
    }
    val currentState: NewsStatus
    fun getNews(isNewPage: Boolean) : Flow<NewsStatus>
    fun restoreNews(newsStatus: NewsStatus) : Flow<NewsStatus>
}

class NewsInteractorImpl(private val newsRepository: NewsRepository) : NewsInteractor{
    override var currentState: NewsStatus
        get() = newsRepository.currentState
        private set(value) {
            newsRepository.currentState = value
        }

    override fun getNews(isNewPage: Boolean) = newsRepository.getNews(isNewPage)

    override fun restoreNews(newsStatus: NewsStatus): Flow<NewsStatus>{
        currentState = newsStatus
        return newsRepository.restoreNews()
    }
}