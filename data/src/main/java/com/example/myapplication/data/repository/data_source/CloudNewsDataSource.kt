package com.example.myapplication.data.repository.data_source

import com.example.myapplication.data.net.ConnectionManager
import com.example.myapplication.data.net.NewsService
import com.example.myapplication.data.status.DataSourceNewsStatus
import kotlinx.coroutines.CancellationException
import java.lang.Exception

class CloudNewsDataSource(private val newsService: NewsService, private val connectionManager: ConnectionManager) : NewsDataSource {
    override suspend fun getNews(
        pageNumber: Int,
        pageSize: Int,
        country: String,
        category: String
    ) = try {
        if(connectionManager.isNetworkAbsent()) DataSourceNewsStatus.NetworkUnavailable
        else {
            val response = newsService.getNews(pageNumber, pageSize, country, category)
            when{
                response.status != "ok" -> DataSourceNewsStatus.Error
                else -> DataSourceNewsStatus.Success(response.articles ?: emptyList())
            }
        }
    }catch (e: Exception){
        e.printStackTrace()
        if(e !is CancellationException)
            DataSourceNewsStatus.ServerUnavailable
        else
            throw e
    }
}