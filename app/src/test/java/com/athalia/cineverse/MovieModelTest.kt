package com.athalia.cineverse

import com.athalia.cineverse.data.model.Genre
import com.athalia.cineverse.data.model.Movie
import com.athalia.cineverse.data.model.MovieDetail
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class MovieModelTest {

    @Test
    fun testMovieProperties() {
        val movie = Movie(
            id = 1726,
            title = "Iron Man",
            overview = "Billionaire Tony Stark builds armor.",
            posterPath = "/78lPtwv72eTNqFW9COBYI0dWDJa.jpg",
            backdropPath = "/cyecB7godJ6kNHGONvOHVJz9L50.jpg",
            voteAverage = 7.68,
            releaseDate = "2008-05-02"
        )

        assertEquals("Iron Man", movie.displayTitle)
        assertEquals("7.7", movie.formattedRating)
        assertEquals("2008", movie.releaseYear)
        assertEquals("https://image.tmdb.org/t/p/w500/78lPtwv72eTNqFW9COBYI0dWDJa.jpg", movie.fullPosterUrl)
        assertEquals("https://image.tmdb.org/t/p/w780/cyecB7godJ6kNHGONvOHVJz9L50.jpg", movie.fullBackdropUrl)
    }

    @Test
    fun testMovieDetailProperties() {
        val detail = MovieDetail(
            id = 1726,
            title = "Iron Man",
            originalTitle = "Iron Man",
            overview = "A genius billionaire...",
            posterPath = "/78lPtwv72eTNqFW9COBYI0dWDJa.jpg",
            backdropPath = "/cyecB7godJ6kNHGONvOHVJz9L50.jpg",
            voteAverage = 7.7,
            voteCount = 25000,
            releaseDate = "2008-05-02",
            runtime = 126,
            genres = listOf(
                Genre(id = 28, name = "Action"),
                Genre(id = 878, name = "Sci-Fi")
            )
        )

        assertEquals("Iron Man", detail.displayTitle)
        assertEquals("7.7", detail.formattedRating)
        assertEquals("2008", detail.releaseYear)
        assertEquals(2, detail.genres.size)
        assertEquals("Action", detail.genres[0].name)
    }
}
