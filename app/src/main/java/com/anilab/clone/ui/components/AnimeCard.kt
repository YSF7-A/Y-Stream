package com.anilab.clone.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.anilab.clone.data.model.Anime
import com.anilab.clone.ui.theme.ChipDubBackground
import com.anilab.clone.ui.theme.ChipDubText
import com.anilab.clone.ui.theme.ChipSubBackground
import com.anilab.clone.ui.theme.ChipSubText
import com.anilab.clone.ui.theme.DarkSurfaceElevated
import com.anilab.clone.ui.theme.StarGold
import com.anilab.clone.ui.theme.TextPrimary
import com.anilab.clone.ui.theme.TextSecondary

@Composable
fun AnimeCard(
    anime: Anime,
    onClick: (Anime) -> Unit,
    modifier: Modifier = Modifier,
    cardWidth: Int = 135
) {
    Column(
        modifier = modifier
            .width(cardWidth.dp)
            .clickable { onClick(anime) }
    ) {
        // Poster Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f)
                .clip(RoundedCornerShape(8.dp))
                .background(DarkSurfaceElevated)
        ) {
            AsyncImage(
                model = anime.poster,
                contentDescription = anime.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Top-right Rating Badge
            if (!anime.rating.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .background(
                            Color.Black.copy(alpha = 0.75f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "★ ${anime.rating}",
                        color = StarGold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Bottom-left Sub / Dub Pills
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(6.dp)
            ) {
                if (anime.episodeInfo?.sub != null && anime.episodeInfo.sub > 0) {
                    Box(
                        modifier = Modifier
                            .background(
                                ChipSubBackground.copy(alpha = 0.9f),
                                shape = RoundedCornerShape(3.dp)
                            )
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "SUB ${anime.episodeInfo.sub}",
                            color = ChipSubText,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (anime.episodeInfo?.dub != null && anime.episodeInfo.dub > 0) {
                    Spacer(modifier = Modifier.width(3.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                ChipDubBackground.copy(alpha = 0.9f),
                                shape = RoundedCornerShape(3.dp)
                            )
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "DUB ${anime.episodeInfo.dub}",
                            color = ChipDubText,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Title
        Text(
            text = anime.name,
            color = TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 16.sp
        )

        // Type / Year
        Text(
            text = anime.type ?: "TV",
            color = TextSecondary,
            fontSize = 11.sp,
            maxLines = 1
        )
    }
}
