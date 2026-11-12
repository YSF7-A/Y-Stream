package com.anilab.clone.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Library : Screen("library")

    object Detail : Screen("detail/{animeId}") {
        fun createRoute(animeId: String) = "detail/$animeId"
    }

    object Player : Screen("player/{animeId}/{episodeId}/{episodeNumber}") {
        fun createRoute(animeId: String, episodeId: String, episodeNumber: Int) =
            "player/$animeId/$episodeId/$episodeNumber"
    }
}
