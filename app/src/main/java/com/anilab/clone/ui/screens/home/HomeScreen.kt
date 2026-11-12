package com.anilab.clone.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anilab.clone.data.model.Anime
import com.anilab.clone.ui.components.AnimeRow
import com.anilab.clone.ui.components.SpotlightBanner
import com.anilab.clone.ui.theme.AccentRed
import com.anilab.clone.ui.theme.DarkBackground
import com.anilab.clone.ui.theme.TextPrimary

@Composable
fun HomeScreen(
    onAnimeClick: (String) -> Unit,
    onWatchClick: (String) -> Unit,
    viewModel: HomeViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        when (val state = uiState) {
            is HomeUiState.Loading -> {
                CircularProgressIndicator(
                    color = AccentRed,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is HomeUiState.Error -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = state.message, color = TextPrimary)
                }
            }

            is HomeUiState.Success -> {
                val feed = state.feed
                val spotlightAnime = feed.spotlight.firstOrNull() ?: feed.trending.firstOrNull()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Spotlight Banner
                    if (spotlightAnime != null) {
                        SpotlightBanner(
                            anime = spotlightAnime,
                            onWatchClick = { anime -> onWatchClick(anime.id) },
                            onDetailClick = { anime -> onAnimeClick(anime.id) }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Trending Now
                    AnimeRow(
                        title = "🔥 Trending Now",
                        animeList = feed.trending,
                        onAnimeClick = { anime -> onAnimeClick(anime.id) }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Top Airing
                    AnimeRow(
                        title = "⭐ Top Airing",
                        animeList = feed.topAiring,
                        onAnimeClick = { anime -> onAnimeClick(anime.id) }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Latest Episodes
                    AnimeRow(
                        title = "⚡ Latest Episodes",
                        animeList = feed.latestEpisodes,
                        onAnimeClick = { anime -> onAnimeClick(anime.id) }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Most Popular
                    AnimeRow(
                        title = "👑 Most Popular",
                        animeList = feed.mostPopular,
                        onAnimeClick = { anime -> onAnimeClick(anime.id) }
                    )

                    Spacer(modifier = Modifier.height(80.dp)) // Bottom padding for navigation bar
                }
            }
        }
    }
}
