package com.example.newsapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *  * "source": {
 *  * "id": null,
 *  * "name": "[Removed]"
 *  * },
 */
@Serializable
data class Source(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
)
