package com.example.newsapi.models

import com.example.newsapi.utils.DateTimeUTCSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

/**
 * "source": {
 * "id": null,
 * "name": "[Removed]"
 * },
 * "author": null,
 * "title": "[Removed]",
 * "description": "[Removed]",
 * "url": "https://removed.com",
 * "urlToImage": null,
 * "publishedAt": "1970-01-01T00:00:00Z",
 * "content": "[Removed]"
 */
@Serializable
data class ArticleDTO(
    @SerialName("source") val source: SourceDTO,
    @SerialName("author") val author: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("url") val url: String,
    @SerialName("urlToImage") val urlToImage: String,
    @SerialName("publishedAt") @Serializable(with = DateTimeUTCSerializer::class) val publishedAt: Date,
    @SerialName("content") val content: String,
)
