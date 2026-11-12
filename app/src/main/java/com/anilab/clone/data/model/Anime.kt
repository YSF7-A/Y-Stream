package com.anilab.clone.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Anime(
    val id: String,
    val name: String,
    val poster: String,
    val type: String? = "TV",
    val rating: String? = null,
    val description: String? = null,
    val rank: Int? = null,
    @SerialName("episodes")
    val episodeInfo: EpisodeCount? = null
)

@Serializable
data class EpisodeCount(
    val sub: Int? = 0,
    val dub: Int? = 0
)

@Serializable
data class AnimeResponse<T>(
    val success: Boolean = true,
    val data: T
)

@Serializable
data class HomeFeed(
    val spotlight: List<Anime> = emptyList(),
    val trending: List<Anime> = emptyList(),
    val latestEpisodes: List<Anime> = emptyList(),
    val topAiring: List<Anime> = emptyList(),
    val mostPopular: List<Anime> = emptyList()
)
