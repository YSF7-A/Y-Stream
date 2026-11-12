package com.anilab.clone.data.repository

import com.anilab.clone.data.api.AnimeApiService
import com.anilab.clone.data.api.RetrofitClient
import com.anilab.clone.data.model.Anime
import com.anilab.clone.data.model.AnimeDetail
import com.anilab.clone.data.model.Episode
import com.anilab.clone.data.model.EpisodeCount
import com.anilab.clone.data.model.HomeFeed
import com.anilab.clone.data.model.StreamSource
import com.anilab.clone.data.model.SubtitleTrack
import com.anilab.clone.data.model.TimestampRange
import com.anilab.clone.data.model.VideoSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnimeRepository(
    private val api: AnimeApiService = RetrofitClient.apiService
) {

    suspend fun getHomeFeed(): Result<HomeFeed> = withContext(Dispatchers.IO) {
        try {
            val response = api.getHome()
            if (response.spotlight.isNotEmpty() || response.trending.isNotEmpty()) {
                Result.success(response)
            } else {
                Result.success(getMockHomeFeed())
            }
        } catch (e: Exception) {
            // Fallback to rich offline/demo feed for testing
            Result.success(getMockHomeFeed())
        }
    }

    suspend fun getAnimeDetail(id: String): Result<AnimeDetail> = withContext(Dispatchers.IO) {
        try {
            val response = api.getAnimeDetails(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.success(getMockAnimeDetail(id))
        }
    }

    suspend fun searchAnime(query: String): Result<List<Anime>> = withContext(Dispatchers.IO) {
        try {
            val results = api.search(query)
            Result.success(results)
        } catch (e: Exception) {
            val mockMatches = getMockHomeFeed().trending.filter {
                it.name.contains(query, ignoreCase = true)
            }
            Result.success(mockMatches)
        }
    }

    suspend fun getEpisodeSources(
        episodeId: String,
        category: String = "sub"
    ): Result<StreamSource> = withContext(Dispatchers.IO) {
        try {
            val sources = api.getSources(episodeId = episodeId, category = category)
            if (sources.sources.isNotEmpty()) {
                Result.success(sources)
            } else {
                Result.success(getMockStreamSource())
            }
        } catch (e: Exception) {
            Result.success(getMockStreamSource())
        }
    }

    // High quality mock data matching AniLab's spotlight & top lists
    private fun getMockHomeFeed(): HomeFeed {
        val spotlightList = listOf(
            Anime(
                id = "solo-leveling-season-2-19253",
                name = "Solo Leveling: Arise from the Shadow",
                poster = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=800",
                type = "TV",
                rating = "8.85",
                rank = 1,
                description = "They say whatever does not kill you makes you stronger. In Sung Jinwoo's case, he awakens an infinite system of strength.",
                episodeInfo = EpisodeCount(sub = 12, dub = 12)
            ),
            Anime(
                id = "jujutsu-kaisen-season-2-18413",
                name = "Jujutsu Kaisen Season 2",
                poster = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=800",
                type = "TV",
                rating = "8.92",
                rank = 2,
                description = "The past comes to light as Satoru Gojo and Suguru Geto take on a critical mission regarding the Star Plasma Vessel.",
                episodeInfo = EpisodeCount(sub = 23, dub = 23)
            ),
            Anime(
                id = "demon-slayer-hashira-training-19125",
                name = "Demon Slayer: Hashira Training Arc",
                poster = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=800",
                type = "TV",
                rating = "8.65",
                rank = 3,
                description = "Tanjiro visits the Stone Hashira, Himejima, to prepare for the inevitable upcoming battle against Muzan Kibutsuji.",
                episodeInfo = EpisodeCount(sub = 8, dub = 8)
            )
        )

        val trendingList = listOf(
            Anime(
                id = "frieren-beyond-journeys-end-18418",
                name = "Frieren: Beyond Journey's End",
                poster = "https://images.unsplash.com/photo-1563089145-599997674d42?w=500",
                type = "TV",
                rating = "9.12",
                episodeInfo = EpisodeCount(sub = 28, dub = 28)
            ),
            Anime(
                id = "chainsaw-man-17483",
                name = "Chainsaw Man",
                poster = "https://images.unsplash.com/photo-1563245372-f21724e3856d?w=500",
                type = "TV",
                rating = "8.60",
                episodeInfo = EpisodeCount(sub = 12, dub = 12)
            ),
            Anime(
                id = "bleach-thousand-year-blood-war-18037",
                name = "Bleach: Thousand-Year Blood War",
                poster = "https://images.unsplash.com/photo-1618336753974-aae8e04506aa?w=500",
                type = "TV",
                rating = "9.05",
                episodeInfo = EpisodeCount(sub = 26, dub = 26)
            ),
            Anime(
                id = "attack-on-titan-final-season-17502",
                name = "Attack on Titan: The Final Season",
                poster = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=500",
                type = "TV",
                rating = "9.02",
                episodeInfo = EpisodeCount(sub = 28, dub = 28)
            )
        )

        return HomeFeed(
            spotlight = spotlightList,
            trending = trendingList,
            latestEpisodes = trendingList.reversed(),
            topAiring = spotlightList,
            mostPopular = trendingList
        )
    }

    private fun getMockAnimeDetail(id: String): AnimeDetail {
        val episodes = (1..24).map { epNum ->
            Episode(
                id = "$id-ep-$epNum",
                number = epNum,
                title = "Episode $epNum: The Gathering Storm",
                isFiller = epNum % 7 == 0
            )
        }

        return AnimeDetail(
            id = id,
            name = "Solo Leveling: Arise from the Shadow",
            poster = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=800",
            banner = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=1200",
            description = "In a world where hunters, humans who possess magical powers, must battle deadly monsters to protect mankind from certain destruction, a notoriously weak hunter named Sung Jinwoo finds himself in a struggle for survival.",
            genres = listOf("Action", "Fantasy", "Supernatural", "Adventure"),
            status = "Completed",
            type = "TV",
            studios = listOf("A-1 Pictures"),
            rating = "8.85",
            totalEpisodes = 24,
            episodes = episodes,
            recommended = getMockHomeFeed().trending
        )
    }

    private fun getMockStreamSource(): StreamSource {
        return StreamSource(
            headers = mapOf(
                "Referer" to "https://megacloud.tv"
            ),
            sources = listOf(
                VideoSource(
                    url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                    isM3U8 = false,
                    quality = "1080p (Full HD)"
                ),
                VideoSource(
                    url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                    isM3U8 = false,
                    quality = "720p (HD)"
                )
            ),
            subtitles = listOf(
                SubtitleTrack(
                    url = "https://example.com/sub/en.vtt",
                    lang = "English",
                    default = true
                ),
                SubtitleTrack(
                    url = "https://example.com/sub/es.vtt",
                    lang = "Spanish",
                    default = false
                )
            ),
            intro = TimestampRange(start = 0, end = 90),
            outro = TimestampRange(start = 1260, end = 1350)
        )
    }
}
