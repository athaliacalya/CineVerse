package com.athalia.cineverse.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.athalia.cineverse.R
import com.athalia.cineverse.data.model.Genre
import com.athalia.cineverse.data.model.Movie
import com.athalia.cineverse.data.model.MovieDetail
import com.athalia.cineverse.data.model.SavedMovie
import com.athalia.cineverse.data.model.toMovie
import com.athalia.cineverse.ui.components.ErrorState
import com.athalia.cineverse.ui.components.GenreChip
import com.athalia.cineverse.ui.components.LoadingState
import com.athalia.cineverse.ui.components.RatingBar
import com.athalia.cineverse.ui.theme.CineVerseTheme
import com.athalia.cineverse.ui.theme.PrimaryPink
import com.athalia.cineverse.ui.theme.PrimaryPinkDark
import com.athalia.cineverse.ui.theme.StarPink
import com.athalia.cineverse.viewmodel.LibraryViewModel
import com.athalia.cineverse.viewmodel.MovieDetailViewModel

@Composable
fun DetailScreen(
    movieId: Int,
    viewModel: MovieDetailViewModel,
    libraryViewModel: LibraryViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(movieId) {
        viewModel.loadDetail(movieId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val savedItems by libraryViewModel.items.collectAsState()
    val savedEntry = savedItems.find { it.movie.id == movieId }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier
    ) { _ ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            when {
                uiState.isLoading && uiState.movieDetail == null -> {
                    LoadingState()
                }

                uiState.errorMessage != null && uiState.movieDetail == null -> {
                    ErrorState(
                        message = uiState.errorMessage ?: stringResource(R.string.error_loading),
                        onRetry = { viewModel.retry() }
                    )
                }

                uiState.movieDetail != null -> {
                    val detail = uiState.movieDetail!!
                    val movie = detail.toMovie()

                    DetailContent(
                        movie = detail,
                        savedMovie = savedEntry,
                        onToggleWatchlist = { libraryViewModel.toggleWatchlist(movie) },
                        onToggleWatched = { libraryViewModel.toggleWatched(movie) },
                        onRateMovie = { libraryViewModel.setRating(movie, it) },
                        onSaveReview = { libraryViewModel.setReview(movie, it) },
                        onBackClick = onBackClick
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun DetailContent(
    movie: MovieDetail,
    savedMovie: SavedMovie? = null,
    onToggleWatchlist: () -> Unit = {},
    onToggleWatched: () -> Unit = {},
    onRateMovie: (Float) -> Unit = {},
    onSaveReview: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var reviewText by remember(savedMovie?.review) { mutableStateOf(savedMovie?.review ?: "") }
    var isReviewFocused by remember { mutableStateOf(false) }
    val isImeVisible = WindowInsets.isImeVisible

    LaunchedEffect(isImeVisible) {
        if (isImeVisible && isReviewFocused) {
            delay(250)
            bringIntoViewRequester.bringIntoView()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imeNestedScroll()
            .verticalScroll(scrollState)
    ) {
        // Backdrop Section with Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            if (!movie.fullBackdropUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = movie.fullBackdropUrl,
                    contentDescription = movie.displayTitle,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(PrimaryPinkDark, MaterialTheme.colorScheme.background)
                            )
                        )
                )
            }

            // Gradient overlay fading into background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.35f),
                                Color.Transparent,
                                MaterialTheme.colorScheme.background.copy(alpha = 0.85f),
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
            )

            // Top Back Button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(start = 16.dp, top = 12.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.45f))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = Color.White
                )
            }
        }

        // Poster and Title Section (Overlapping Backdrop)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = (-50).dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // Poster Card
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .width(115.dp)
                    .aspectRatio(0.68f)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (!movie.fullPosterUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = movie.fullPosterUrl,
                            contentDescription = movie.displayTitle,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Movie,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Title, Rating, and Year
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
            ) {
                Text(
                    text = movie.displayTitle,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = StarPink,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${movie.formattedRating} · ${movie.releaseYear}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Content below poster
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = (-30).dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Genre Chips
            if (movie.genres.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(movie.genres) { genre ->
                        GenreChip(name = genre.name)
                    }
                }
            }

            // Letterboxd Action Buttons (Watchlist & Watched)
            val isWatchlist = savedMovie?.watchlist == true
            val isWatched = savedMovie?.watched == true

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilledTonalButton(
                    onClick = onToggleWatchlist,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = if (isWatchlist) PrimaryPink.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isWatchlist) PrimaryPink else MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (isWatchlist) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = null,
                        tint = if (isWatchlist) PrimaryPink else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.watchlist),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }

                FilledTonalButton(
                    onClick = onToggleWatched,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = if (isWatched) PrimaryPink.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isWatched) PrimaryPink else MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    if (isWatched) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = PrimaryPink,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    Text(
                        text = stringResource(R.string.watched),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }

            // Your Rating (RatingBar)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = stringResource(R.string.your_rating),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                RatingBar(
                    rating = savedMovie?.rating ?: 0f,
                    onRate = onRateMovie
                )
            }

            // Sinopsis & Review Section (dibungkus agar tidak tertutup keyboard)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .bringIntoViewRequester(bringIntoViewRequester),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Sinopsis Section
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.synopsis),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        text = if (!movie.overview.isNullOrBlank()) {
                            movie.overview
                        } else {
                            stringResource(R.string.no_synopsis)
                        },
                        style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Your Review Section
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.your_review),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    OutlinedTextField(
                        value = reviewText,
                        onValueChange = { reviewText = it },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.review_hint),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                isReviewFocused = focusState.isFocused
                                if (focusState.isFocused) {
                                    coroutineScope.launch {
                                        delay(300)
                                        bringIntoViewRequester.bringIntoView()
                                    }
                                }
                            },
                        minLines = 3,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPink,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Button(
                        onClick = {
                            onSaveReview(reviewText)
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPink),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            text = stringResource(R.string.save),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp).navigationBarsPadding())
        }
    }
}

private val sampleMovieDetail = MovieDetail(
    id = 1726,
    title = "Iron Man",
    overview = "Menceritakan kisah Tony Stark, seorang industrialis miliarder dan penemu jenius yang diculik dan dipaksa untuk membuat senjata yang mematikan. Dengan kecerdasannya, ia membangun baju zirah berteknologi tinggi.",
    voteAverage = 7.7,
    voteCount = 25000,
    releaseDate = "2008-05-02",
    runtime = 126,
    genres = listOf(
        Genre(id = 28, name = "Action"),
        Genre(id = 878, name = "Sci-Fi"),
        Genre(id = 12, name = "Adventure")
    )
)

private val sampleSavedMovie = SavedMovie(
    movie = sampleMovieDetail.toMovie(),
    watchlist = true,
    watched = true,
    rating = 4f,
    review = "Film superhero klasik yang mengawali MCU dengan sangat solid!"
)

@Preview(name = "Detail Film - Light", showBackground = true)
@Composable
fun DetailScreenLightPreview() {
    CineVerseTheme(darkTheme = false) {
        Surface {
            DetailContent(
                movie = sampleMovieDetail,
                savedMovie = sampleSavedMovie,
                onBackClick = {}
            )
        }
    }
}

@Preview(name = "Detail Film - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenDarkPreview() {
    CineVerseTheme(darkTheme = true) {
        Surface {
            DetailContent(
                movie = sampleMovieDetail,
                savedMovie = sampleSavedMovie,
                onBackClick = {}
            )
        }
    }
}
