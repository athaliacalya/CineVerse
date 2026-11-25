package com.athalia.cineverse.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.athalia.cineverse.R
import com.athalia.cineverse.ui.components.CategorySection
import com.athalia.cineverse.ui.components.ErrorState
import com.athalia.cineverse.ui.components.LoadingState
import com.athalia.cineverse.ui.components.TrendingBanner
import com.athalia.cineverse.ui.theme.PrimaryPink
import com.athalia.cineverse.viewmodel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: MovieViewModel,
    onMovieClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

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
                        message = uiState.errorMessage ?: stringResource(R.string.error_loading),
                        onRetry = { viewModel.retry() }
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        // Trending Banner
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

                        // Popular Section
                        item {
                            CategorySection(
                                title = stringResource(R.string.popular),
                                movies = uiState.popularMovies,
                                onMovieClick = onMovieClick
                            )
                        }

                        // Now Playing Section
                        item {
                            CategorySection(
                                title = stringResource(R.string.now_playing),
                                movies = uiState.nowPlayingMovies,
                                onMovieClick = onMovieClick
                            )
                        }

                        // Top Rated Section
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
