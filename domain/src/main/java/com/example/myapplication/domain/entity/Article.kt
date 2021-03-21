package com.example.myapplication.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val sourceName: String,
    val title: String,
    val url: String,
    val publishedAt: String,
    val content: String?,
    val author: String?,
    val urlToImage: String?,
)
