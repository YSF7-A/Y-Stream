package com.anilab.clone.data.model

import kotlinx.serialization.Serializable

@Serializable
data class StreamSource(
    val headers: Map<String, String> = emptyMap(),
    val sources: List<VideoSource> = emptyList(),
    val subtitles: List<SubtitleTrack> = emptyList(),
    val intro: TimestampRange? = null,
    val outro: TimestampRange? = null
)

@Serializable
data class VideoSource(
    val url: String,
    val isM3U8: Boolean = true,
    val quality: String = "auto"
)

@Serializable
data class SubtitleTrack(
    val url: String,
    val lang: String,
    val default: Boolean = false
)

@Serializable
data class TimestampRange(
    val start: Long,
    val end: Long
)

@Serializable
data class ServerItem(
    val serverId: Int,
    val serverName: String,
    val type: String // "sub" or "dub"
)

@Serializable
data class EpisodeServers(
    val episodeId: String,
    val episodeNo: Int,
    val sub: List<ServerItem> = emptyList(),
    val dub: List<ServerItem> = emptyList(),
    val raw: List<ServerItem> = emptyList()
)
