package com.athalia.cineverse.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.athalia.cineverse.R
import com.athalia.cineverse.data.model.Movie
import com.athalia.cineverse.data.model.SavedMovie
import com.athalia.cineverse.ui.components.MovieCard
import com.athalia.cineverse.ui.theme.CineVerseTheme
import com.athalia.cineverse.ui.theme.PrimaryPink
import com.athalia.cineverse.viewmodel.LibraryViewModel
import java.util.Locale

@Composable
fun CollectionScreen(
    libraryViewModel: LibraryViewModel,
    onMovieClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val savedItems by libraryViewModel.items.collectAsState()

    CollectionContent(
        savedItems = savedItems,
        onMovieClick = onMovieClick,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionContent(
    savedItems: List<SavedMovie>,
    onMovieClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    val currentList = if (selectedTab == 0) {
        savedItems.filter { it.watchlist }
    } else {
        savedItems.filter { it.watched }
    }

    val watchedMovies = savedItems.filter { it.watched }
    val ratedMovies = watchedMovies.filter { it.rating > 0f }
    val averageRating = if (ratedMovies.isNotEmpty()) {
        ratedMovies.map { it.rating }.average()
    } else {
        0.0
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.my_collection),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Stats bar ala Letterboxd profile
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(
                        R.string.stats,
                        watchedMovies.size,
                        String.format(Locale.US, "%.1f", averageRating)
                    ),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            // TabRow (Watchlist vs Ditonton)
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = PrimaryPink,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = PrimaryPink
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "${stringResource(R.string.watchlist)} (${savedItems.count { it.watchlist }})",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (selectedTab == 0) PrimaryPink else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "${stringResource(R.string.watched)} (${watchedMovies.size})",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (selectedTab == 1) PrimaryPink else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }

            // Content Grid or Empty State
            if (currentList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.empty_collection),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 135.dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(currentList, key = { it.movie.id }) { saved ->
                        MovieCard(
                            movie = saved.movie,
                            onMovieClick = onMovieClick,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

private val sampleCollectionMovies = listOf(
    SavedMovie(
        movie = Movie(id = 1726, title = "Iron Man", voteAverage = 7.7, releaseDate = "2008-05-02"),
        watchlist = true,
        watched = true,
        rating = 4f
    ),
    SavedMovie(
        movie = Movie(id = 155, title = "The Dark Knight", voteAverage = 8.5, releaseDate = "2008-07-18"),
        watchlist = false,
        watched = true,
        rating = 5f
    )
)

@Preview(name = "Collection - Light", showBackground = true)
@Composable
fun CollectionScreenLightPreview() {
    CineVerseTheme(darkTheme = false) {
        Surface {
            CollectionContent(
                savedItems = sampleCollectionMovies,
                onMovieClick = {},
                onBackClick = {}
            )
        }
    }
}

@Preview(name = "Collection - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun CollectionScreenDarkPreview() {
    CineVerseTheme(darkTheme = true) {
        Surface {
            CollectionContent(
                savedItems = sampleCollectionMovies,
                onMovieClick = {},
                onBackClick = {}
            )
        }
    }
}
