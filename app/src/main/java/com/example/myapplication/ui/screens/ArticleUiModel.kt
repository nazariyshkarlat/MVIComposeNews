package com.example.myapplication.ui.screens

import com.example.myapplication.domain.entity.Article
import com.example.myapplication.formatDate
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Serializable
data class ArticleUiModel(
    val sourceName: String,
    val title: String,
    val url: String,
    val publishedAt: String,
    val content: String?,
    val author: String?,
    val urlToImage: String,
)

fun Article.toUiModel() = ArticleUiModel(
    sourceName = sourceName,
    title = title,
    url = url,
    publishedAt = formatDate(publishedAt),
    content = content,
    author = author,
    urlToImage = urlToImage ?: "https://www.generationsforpeace.org/wp-content/uploads/2018/03/empty.jpg"
)
