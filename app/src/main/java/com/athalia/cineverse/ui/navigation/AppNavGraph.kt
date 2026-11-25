package com.athalia.cineverse.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.athalia.cineverse.ui.screens.DashboardScreen
import com.athalia.cineverse.ui.screens.DetailScreen
import com.athalia.cineverse.ui.screens.SearchScreen
import com.athalia.cineverse.ui.screens.SettingsScreen
import com.athalia.cineverse.viewmodel.MovieDetailViewModel
import com.athalia.cineverse.viewmodel.MovieViewModel
import com.athalia.cineverse.viewmodel.SearchViewModel
import com.athalia.cineverse.viewmodel.SettingsViewModel
import com.athalia.cineverse.viewmodel.ViewModelFactory

@Composable
fun AppNavGraph(
    navController: NavHostController,
    factory: ViewModelFactory,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(Screen.Dashboard.route) {
            val movieViewModel: MovieViewModel = viewModel(factory = factory)
            DashboardScreen(
                viewModel = movieViewModel,
                onMovieClick = { movieId ->
                    navController.navigate(Screen.Detail.createRoute(movieId))
                },
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            val detailViewModel: MovieDetailViewModel = viewModel(factory = factory)
            DetailScreen(
                movieId = movieId,
                viewModel = detailViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            val settingsViewModel: SettingsViewModel = viewModel(factory = factory)
            SettingsScreen(
                viewModel = settingsViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Search.route) {
            val searchViewModel: SearchViewModel = viewModel(factory = factory)
            SearchScreen(
                viewModel = searchViewModel,
                onMovieClick = { movieId ->
                    navController.navigate(Screen.Detail.createRoute(movieId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
