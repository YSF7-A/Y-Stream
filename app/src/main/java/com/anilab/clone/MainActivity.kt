package com.anilab.clone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.anilab.clone.ui.navigation.Screen
import com.anilab.clone.ui.screens.detail.DetailScreen
import com.anilab.clone.ui.screens.home.HomeScreen
import com.anilab.clone.ui.screens.library.LibraryScreen
import com.anilab.clone.ui.screens.player.PlayerScreen
import com.anilab.clone.ui.screens.search.SearchScreen
import com.anilab.clone.ui.theme.AccentRed
import com.anilab.clone.ui.theme.AniLabTheme
import com.anilab.clone.ui.theme.DarkBackground
import com.anilab.clone.ui.theme.DarkSurface
import com.anilab.clone.ui.theme.TextPrimary
import com.anilab.clone.ui.theme.TextSecondary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AniLabTheme {
                MainRootView()
            }
        }
    }
}

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)

@Composable
fun MainRootView() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        BottomNavItem(Screen.Home.route, "Home", Icons.Default.Home),
        BottomNavItem(Screen.Search.route, "Search", Icons.Default.Search),
        BottomNavItem(Screen.Library.route, "Library", Icons.Default.VideoLibrary)
    )

    // Hide bottom bar when watching video or on detail screen
    val shouldShowBottomBar = currentRoute in bottomNavItems.map { it.route }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkBackground,
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar(
                    containerColor = DarkSurface,
                    contentColor = TextPrimary
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentRoute == item.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(item.title) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = AccentRed,
                                selectedTextColor = AccentRed,
                                indicatorColor = DarkSurface,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Home
            composable(Screen.Home.route) {
                HomeScreen(
                    onAnimeClick = { animeId ->
                        navController.navigate(Screen.Detail.createRoute(animeId))
                    },
                    onWatchClick = { animeId ->
                        navController.navigate(
                            Screen.Player.createRoute(animeId, "$animeId-ep-1", 1)
                        )
                    }
                )
            }

            // Search
            composable(Screen.Search.route) {
                SearchScreen(
                    onAnimeClick = { animeId ->
                        navController.navigate(Screen.Detail.createRoute(animeId))
                    }
                )
            }

            // Library
            composable(Screen.Library.route) {
                LibraryScreen(
                    onAnimeClick = { animeId ->
                        navController.navigate(Screen.Detail.createRoute(animeId))
                    }
                )
            }

            // Anime Detail
            composable(
                route = Screen.Detail.route,
                arguments = listOf(
                    navArgument("animeId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val animeId = backStackEntry.arguments?.getString("animeId").orEmpty()
                DetailScreen(
                    animeId = animeId,
                    onBackClick = { navController.popBackStack() },
                    onEpisodeClick = { epId, epNum ->
                        navController.navigate(
                            Screen.Player.createRoute(animeId, epId, epNum)
                        )
                    },
                    onAnimeClick = { recId ->
                        navController.navigate(Screen.Detail.createRoute(recId))
                    }
                )
            }

            // Video Player
            composable(
                route = Screen.Player.route,
                arguments = listOf(
                    navArgument("animeId") { type = NavType.StringType },
                    navArgument("episodeId") { type = NavType.StringType },
                    navArgument("episodeNumber") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val animeId = backStackEntry.arguments?.getString("animeId").orEmpty()
                val episodeId = backStackEntry.arguments?.getString("episodeId").orEmpty()
                val episodeNumber = backStackEntry.arguments?.getInt("episodeNumber") ?: 1

                PlayerScreen(
                    animeId = animeId,
                    episodeId = episodeId,
                    episodeNumber = episodeNumber,
                    onBackClick = { navController.popBackStack() },
                    onNextEpisodeClick = {
                        val nextEpNum = episodeNumber + 1
                        navController.navigate(
                            Screen.Player.createRoute(animeId, "$animeId-ep-$nextEpNum", nextEpNum)
                        ) {
                            popUpTo(Screen.Player.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
