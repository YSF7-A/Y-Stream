package com.anilab.clone.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.anilab.clone.data.model.Episode
import com.anilab.clone.ui.components.AnimeRow
import com.anilab.clone.ui.theme.AccentRed
import com.anilab.clone.ui.theme.DarkBackground
import com.anilab.clone.ui.theme.DarkSurface
import com.anilab.clone.ui.theme.DarkSurfaceElevated
import com.anilab.clone.ui.theme.StarGold
import com.anilab.clone.ui.theme.TextPrimary
import com.anilab.clone.ui.theme.TextSecondary

@Composable
fun DetailScreen(
    animeId: String,
    onBackClick: () -> Unit,
    onEpisodeClick: (String, Int) -> Unit,
    onAnimeClick: (String) -> Unit,
    viewModel: DetailViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(animeId) {
        viewModel.loadAnimeDetail(animeId)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        when (val state = uiState) {
            is DetailUiState.Loading -> {
                CircularProgressIndicator(
                    color = AccentRed,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is DetailUiState.Error -> {
                Text(
                    text = state.message,
                    color = TextPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is DetailUiState.Success -> {
                val anime = state.detail
                var isSynopsisExpanded by remember { mutableStateOf(false) }
                var selectedTabGroup by remember { mutableIntStateOf(0) }

                // Group episodes into batches of 25 (e.g., 1-25, 26-50...)
                val episodeChunks = anime.episodes.chunked(25)

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    // Header Banner with Backdrop
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                        ) {
                            AsyncImage(
                                model = anime.banner ?: anime.poster,
                                contentDescription = anime.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Black.copy(alpha = 0.4f),
                                                DarkBackground.copy(alpha = 0.8f),
                                                DarkBackground
                                            )
                                        )
                                    )
                            )

                            // Top Back Button
                            IconButton(
                                onClick = onBackClick,
                                modifier = Modifier
                                    .padding(top = 16.dp, start = 12.dp)
                                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    // Anime Meta Info (Poster, Title, Rating, Type, Genres)
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            // Poster
                            AsyncImage(
                                model = anime.poster,
                                contentDescription = anime.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .width(105.dp)
                                    .aspectRatio(0.7f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DarkSurfaceElevated)
                            )

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = anime.name,
                                    color = TextPrimary,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 2
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    if (!anime.rating.isNullOrEmpty()) {
                                        Text(
                                            text = "★ ${anime.rating}",
                                            color = StarGold,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Text(
                                        text = anime.type ?: "TV",
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = anime.status ?: "Finished",
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Genres
                                Text(
                                    text = anime.genres.joinToString(", "),
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    // Watch Now / Action buttons
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    val firstEp = anime.episodes.firstOrNull()
                                    if (firstEp != null) {
                                        onEpisodeClick(firstEp.id, firstEp.number)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Watch",
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Play Episode 1", fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { /* Add to bookmarks */ },
                                shape = CircleShape,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BookmarkBorder,
                                    contentDescription = "Bookmark",
                                    tint = TextPrimary
                                )
                            }

                            OutlinedButton(
                                onClick = { /* Share */ },
                                shape = CircleShape,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = TextPrimary
                                )
                            }
                        }
                    }

                    // Synopsis
                    if (!anime.description.isNullOrEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                Text(
                                    text = "Overview",
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = anime.description,
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    maxLines = if (isSynopsisExpanded) Int.MAX_VALUE else 3,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.clickable {
                                        isSynopsisExpanded = !isSynopsisExpanded
                                    }
                                )
                                Text(
                                    text = if (isSynopsisExpanded) "Show Less" else "Read More",
                                    color = AccentRed,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                        .padding(top = 4.dp)
                                        .clickable { isSynopsisExpanded = !isSynopsisExpanded }
                                )
                            }
                        }
                    }

                    // Episodes Section Header & Tabs
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Episodes (${anime.episodes.size})",
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        if (episodeChunks.size > 1) {
                            ScrollableTabRow(
                                selectedTabIndex = selectedTabGroup,
                                containerColor = DarkBackground,
                                contentColor = AccentRed,
                                edgePadding = 16.dp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                episodeChunks.forEachIndexed { index, list ->
                                    val start = index * 25 + 1
                                    val end = start + list.size - 1
                                    Tab(
                                        selected = selectedTabGroup == index,
                                        onClick = { selectedTabGroup = index },
                                        text = {
                                            Text(
                                                text = "$start - $end",
                                                fontWeight = if (selectedTabGroup == index) FontWeight.Bold else FontWeight.Normal,
                                                color = if (selectedTabGroup == index) AccentRed else TextSecondary
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Episodes List
                    val activeEpisodes = episodeChunks.getOrNull(selectedTabGroup) ?: anime.episodes
                    items(activeEpisodes, key = { it.id }) { ep ->
                        EpisodeItem(
                            episode = ep,
                            onClick = { onEpisodeClick(ep.id, ep.number) }
                        )
                    }

                    // Recommendations
                    if (anime.recommended.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                            AnimeRow(
                                title = "You Might Also Like",
                                animeList = anime.recommended,
                                onAnimeClick = { rec -> onAnimeClick(rec.id) }
                            )
                            Spacer(modifier = Modifier.height(40.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EpisodeItem(
    episode: Episode,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(DarkSurface)
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(AccentRed.copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = AccentRed,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Episode ${episode.number}",
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            if (!episode.title.isNullOrEmpty()) {
                Text(
                    text = episode.title,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (episode.isFiller) {
            Box(
                modifier = Modifier
                    .background(Color.DarkGray, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "Filler",
                    color = Color.LightGray,
                    fontSize = 10.sp
                )
            }
        }
    }
}
