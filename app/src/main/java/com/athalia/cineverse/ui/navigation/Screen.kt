package com.athalia.cineverse.ui.navigation

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Detail : Screen("detail/{movieId}") {
        fun createRoute(movieId: Int): String = "detail/$movieId"
    }
    data object Settings : Screen("settings")
    data object Search : Screen("search")
}
