package com.anilab.clone.ui.screens.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anilab.clone.ui.components.ExoPlayerView
import com.anilab.clone.ui.theme.AccentRed
import com.anilab.clone.ui.theme.DarkBackground
import com.anilab.clone.ui.theme.TextPrimary
import com.anilab.clone.ui.theme.TextSecondary

@Composable
fun PlayerScreen(
    animeId: String,
    episodeId: String,
    episodeNumber: Int,
    onBackClick: () -> Unit,
    onNextEpisodeClick: (() -> Unit)? = null,
    viewModel: PlayerViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(episodeId) {
        viewModel.loadEpisodeStream(episodeId)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        when (val state = uiState) {
            is PlayerUiState.Loading -> {
                CircularProgressIndicator(
                    color = AccentRed,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is PlayerUiState.Error -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = state.message, color = Color.White)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.loadEpisodeStream(episodeId) },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentRed)
                    ) {
                        Text(text = "Retry Stream")
                    }
                }
            }

            is PlayerUiState.Ready -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Video Player
                    Box(modifier = Modifier.weight(1f)) {
                        ExoPlayerView(
                            videoUrl = state.selectedUrl,
                            title = "Episode $episodeNumber",
                            episodeSubtitle = "Streaming via Media3",
                            introRange = state.streamSource.intro,
                            onBackClick = onBackClick,
                            onNextEpisodeClick = onNextEpisodeClick
                        )
                    }

                    // Server & Sub/Dub Switcher Bar (AniLab style)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkBackground)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Audio & Servers",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            FilterChip(
                                selected = state.selectedCategory == "sub",
                                onClick = { viewModel.switchCategory(episodeId, "sub") },
                                label = { Text("SUB", fontWeight = FontWeight.Bold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AccentRed,
                                    selectedLabelColor = Color.White
                                ),
                                shape = RoundedCornerShape(16.dp)
                            )

                            Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                            FilterChip(
                                selected = state.selectedCategory == "dub",
                                onClick = { viewModel.switchCategory(episodeId, "dub") },
                                label = { Text("DUB", fontWeight = FontWeight.Bold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AccentRed,
                                    selectedLabelColor = Color.White
                                ),
                                shape = RoundedCornerShape(16.dp)
                            )
                        }

                        // Quality / Source picker
                        if (state.streamSource.sources.size > 1) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Qualities",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                            Row(modifier = Modifier.padding(top = 4.dp)) {
                                state.streamSource.sources.forEach { source ->
                                    FilterChip(
                                        selected = state.selectedUrl == source.url,
                                        onClick = { viewModel.selectSource(source.url) },
                                        label = { Text(source.quality, fontSize = 11.sp) },
                                        modifier = Modifier.padding(end = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
