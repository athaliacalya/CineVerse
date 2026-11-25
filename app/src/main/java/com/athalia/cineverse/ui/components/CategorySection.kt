package com.athalia.cineverse.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.athalia.cineverse.data.model.Movie
import com.athalia.cineverse.ui.theme.CineVerseTheme

@Composable
fun CategorySection(
    title: String,
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (movies.isEmpty()) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(movies, key = { it.id }) { movie ->
                MovieCard(
                    movie = movie,
                    onMovieClick = onMovieClick
                )
            }
        }
    }
}

private val previewCategoryMovies = listOf(
    Movie(id = 1, title = "Iron Man", voteAverage = 7.7, releaseDate = "2008-05-02"),
    Movie(id = 2, title = "The Dark Knight", voteAverage = 8.5, releaseDate = "2008-07-18"),
    Movie(id = 3, title = "Interstellar", voteAverage = 8.4, releaseDate = "2014-11-07")
)

@Preview(name = "Category Section - Light", showBackground = true)
@Composable
fun CategorySectionLightPreview() {
    CineVerseTheme(darkTheme = false) {
        Surface {
            CategorySection(
                title = "Populer",
                movies = previewCategoryMovies,
                onMovieClick = {}
            )
        }
    }
}

@Preview(name = "Category Section - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun CategorySectionDarkPreview() {
    CineVerseTheme(darkTheme = true) {
        Surface {
            CategorySection(
                title = "Populer",
                movies = previewCategoryMovies,
                onMovieClick = {}
            )
        }
    }
}
