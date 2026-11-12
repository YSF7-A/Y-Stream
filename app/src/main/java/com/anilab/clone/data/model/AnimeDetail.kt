package com.anilab.clone.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AnimeDetail(
    val id: String,
    val name: String,
    val poster: String,
    val banner: String? = null,
    val description: String? = null,
    val genres: List<String> = emptyList(),
    val status: String? = "Completed",
    val type: String? = "TV",
    val studios: List<String> = emptyList(),
    val rating: String? = null,
    val totalEpisodes: Int = 0,
    val episodes: List<Episode> = emptyList(),
    val recommended: List<Anime> = emptyList()
)

@Serializable
data class Episode(
    val id: String,
    val number: Int,
    val title: String? = null,
    val isFiller: Boolean = false,
    val thumbnail: String? = null
)
