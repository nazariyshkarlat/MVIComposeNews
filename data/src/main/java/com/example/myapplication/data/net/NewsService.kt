package com.example.myapplication.data.net

import com.example.myapplication.data.entity.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NewsService {

    @GET("v2/top-headlines")
    suspend fun getNews(
        @Query("page") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String = "a8bf4254d07a4912979d8069feb6ab8a",
    ) : NewsResponse

}