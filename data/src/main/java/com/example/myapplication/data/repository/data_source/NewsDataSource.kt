package com.example.myapplication.data.repository.data_source

import com.example.myapplication.data.status.DataSourceNewsStatus

interface NewsDataSource {

    suspend fun getNews(pageNumber: Int,
                        pageSize: Int,
                        country: String,
                        category: String) : DataSourceNewsStatus

}