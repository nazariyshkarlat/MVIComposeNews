package com.example.myapplication.data.entity

import com.example.myapplication.domain.entity.Article
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    @SerialName("status") val status: String,
    @SerialName("totalResults") val totalResults: Int,
    @SerialName("articles") val articles: List<ArticleEntity>?
)

@Serializable
data class ArticleEntity(
    @SerialName("source") val source: SourceEntity,
    @SerialName("title") val title: String,
    @SerialName("url") val url: String,
    @SerialName("publishedAt") val publishedAt: String,
    @SerialName("content") val content: String?,
    @SerialName("author") val author: String?,
    @SerialName("urlToImage") val urlToImage: String?,
)

@Serializable
data class SourceEntity(
    @SerialName("name") val name: String
)

fun ArticleEntity.toDomain() = Article(
    sourceName = source.name,
    title = title,
    url = url,
    publishedAt = publishedAt,
    content = content,
    author = author,
    urlToImage = urlToImage
)

