package com.athalia.cineverse.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.athalia.cineverse.R
import com.athalia.cineverse.data.model.Movie
import com.athalia.cineverse.ui.components.CategorySection
import com.athalia.cineverse.ui.components.ErrorState
import com.athalia.cineverse.ui.components.LoadingState
import com.athalia.cineverse.ui.components.TrendingBanner
import com.athalia.cineverse.ui.theme.CineVerseTheme
import com.athalia.cineverse.ui.theme.PrimaryPink
import com.athalia.cineverse.viewmodel.DashboardUiState
import com.athalia.cineverse.viewmodel.MovieViewModel

@Composable
fun DashboardScreen(
    viewModel: MovieViewModel,
    onMovieClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    onOpenCollection: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    DashboardContent(
        uiState = uiState,
        onMovieClick = onMovieClick,
        onSearchClick = onSearchClick,
        onOpenCollection = onOpenCollection,
        onSettingsClick = onSettingsClick,
        onRetry = { viewModel.retry() },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardContent(
    uiState: DashboardUiState,
    onMovieClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    onOpenCollection: () -> Unit,
    onSettingsClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp
                        ),
                        color = PrimaryPink
                    )
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.search_title),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    IconButton(onClick = onOpenCollection) {
                        Icon(
                            imageVector = Icons.Default.Bookmarks,
                            contentDescription = stringResource(R.string.my_collection),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.settings),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading && uiState.trendingMovie == null -> {
                    LoadingState()
                }

                uiState.errorMessage != null && uiState.trendingMovie == null -> {
                    ErrorState(
                        message = uiState.errorMessage,
                        onRetry = onRetry
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        uiState.trendingMovie?.let { trending ->
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    TrendingBanner(
                                        movie = trending,
                                        onMovieClick = onMovieClick
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }

                        item {
                            CategorySection(
                                title = stringResource(R.string.popular),
                                movies = uiState.popularMovies,
                                onMovieClick = onMovieClick
                            )
                        }

                        item {
                            CategorySection(
                                title = stringResource(R.string.now_playing),
                                movies = uiState.nowPlayingMovies,
                                onMovieClick = onMovieClick
                            )
                        }

                        item {
                            CategorySection(
                                title = stringResource(R.string.top_rated),
                                movies = uiState.topRatedMovies,
                                onMovieClick = onMovieClick
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }
}

private val sampleMoviesList = listOf(
    Movie(id = 1, title = "Iron Man", voteAverage = 7.7, releaseDate = "2008-05-02"),
    Movie(id = 2, title = "The Dark Knight", voteAverage = 8.5, releaseDate = "2008-07-18"),
    Movie(id = 3, title = "Interstellar", voteAverage = 8.4, releaseDate = "2014-11-07")
)

private val sampleDashboardState = DashboardUiState(
    isLoading = false,
    trendingMovie = sampleMoviesList[0],
    popularMovies = sampleMoviesList,
    nowPlayingMovies = sampleMoviesList,
    topRatedMovies = sampleMoviesList
)

@Preview(name = "Dashboard - Light", showBackground = true)
@Composable
fun DashboardLightPreview() {
    CineVerseTheme(darkTheme = false) {
        Surface {
            DashboardContent(
                uiState = sampleDashboardState,
                onMovieClick = {},
                onSearchClick = {},
                onOpenCollection = {},
                onSettingsClick = {},
                onRetry = {}
            )
        }
    }
}

@Preview(name = "Dashboard - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DashboardDarkPreview() {
    CineVerseTheme(darkTheme = true) {
        Surface {
            DashboardContent(
                uiState = sampleDashboardState,
                onMovieClick = {},
                onSearchClick = {},
                onOpenCollection = {},
                onSettingsClick = {},
                onRetry = {}
            )
        }
    }
}
