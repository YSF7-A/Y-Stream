package com.anilab.clone.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anilab.clone.data.repository.AnimeRepository
import com.anilab.clone.ui.components.AnimeCard
import com.anilab.clone.ui.theme.AccentRed
import com.anilab.clone.ui.theme.DarkBackground
import com.anilab.clone.ui.theme.TextPrimary
import com.anilab.clone.ui.theme.TextSecondary

@Composable
fun LibraryScreen(
    onAnimeClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf("Watchlist", "History", "Downloaded")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // Mock saved library items for display
    val sampleList = remember {
        AnimeRepository().run {
            // grab mock list
            listOf(
                com.anilab.clone.data.model.Anime(
                    id = "solo-leveling-season-2-19253",
                    name = "Solo Leveling: Arise from the Shadow",
                    poster = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=800",
                    type = "TV",
                    rating = "8.85"
                ),
                com.anilab.clone.data.model.Anime(
                    id = "frieren-beyond-journeys-end-18418",
                    name = "Frieren: Beyond Journey's End",
                    poster = "https://images.unsplash.com/photo-1563089145-599997674d42?w=500",
                    type = "TV",
                    rating = "9.12"
                )
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Text(
            text = "My Library",
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 12.dp)
        )

        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = DarkBackground,
            contentColor = AccentRed
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTabIndex == index) AccentRed else TextSecondary,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (sampleList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No anime saved in ${tabs[selectedTabIndex]} yet",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 110.dp),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(sampleList, key = { it.id }) { anime ->
                    AnimeCard(
                        anime = anime,
                        onClick = { onAnimeClick(anime.id) },
                        cardWidth = 110
                    )
                }
            }
        }
    }
}
