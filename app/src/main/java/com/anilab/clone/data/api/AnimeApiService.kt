package com.anilab.clone.data.api

import com.anilab.clone.data.model.Anime
import com.anilab.clone.data.model.AnimeDetail
import com.anilab.clone.data.model.EpisodeServers
import com.anilab.clone.data.model.HomeFeed
import com.anilab.clone.data.model.StreamSource
import retrofit2.http.GET
import retrofit2.http.Query

interface AnimeApiService {

    @GET("api/v2/hianime/home")
    suspend fun getHome(): HomeFeed

    @GET("api/v2/hianime/anime/info")
    suspend fun getAnimeDetails(
        @Query("id") id: String
    ): AnimeDetail

    @GET("api/v2/hianime/search")
    suspend fun search(
        @Query("q") query: String,
        @Query("page") page: Int = 1
    ): List<Anime>

    @GET("api/v2/hianime/episode/servers")
    suspend fun getServers(
        @Query("animeEpisodeId") episodeId: String
    ): EpisodeServers

    @GET("api/v2/hianime/episode/sources")
    suspend fun getSources(
        @Query("animeEpisodeId") episodeId: String,
        @Query("server") server: String = "hd-1",
        @Query("category") category: String = "sub"
    ): StreamSource
}
