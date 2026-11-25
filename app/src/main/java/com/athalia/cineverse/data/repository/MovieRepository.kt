package com.athalia.cineverse.data.repository

import com.athalia.cineverse.data.api.ApiClient
import com.athalia.cineverse.data.api.TmdbApiService
import com.athalia.cineverse.data.model.Genre
import com.athalia.cineverse.data.model.Movie
import com.athalia.cineverse.data.model.MovieDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(
    private val apiService: TmdbApiService = ApiClient.tmdbApiService
) {

    suspend fun getTrendingMovies(language: String = "en-US"): Result<List<Movie>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getTrendingMovies(language = language)
            Result.success(response.results)
        } catch (e: Exception) {
            val fallback = getFallbackMovies(language)
            if (fallback.isNotEmpty()) {
                Result.success(fallback)
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun getPopularMovies(language: String = "en-US"): Result<List<Movie>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getPopularMovies(language = language)
            Result.success(response.results)
        } catch (e: Exception) {
            val fallback = getFallbackMovies(language)
            if (fallback.isNotEmpty()) {
                Result.success(fallback)
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun getNowPlayingMovies(language: String = "en-US"): Result<List<Movie>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getNowPlayingMovies(language = language)
            Result.success(response.results)
        } catch (e: Exception) {
            val fallback = getFallbackMovies(language)
            if (fallback.isNotEmpty()) {
                Result.success(fallback)
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun getTopRatedMovies(language: String = "en-US"): Result<List<Movie>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getTopRatedMovies(language = language)
            Result.success(response.results)
        } catch (e: Exception) {
            val fallback = getFallbackMovies(language)
            if (fallback.isNotEmpty()) {
                Result.success(fallback)
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun getMovieDetail(movieId: Int, language: String = "en-US"): Result<MovieDetail> = withContext(Dispatchers.IO) {
        try {
            val detail = apiService.getMovieDetail(movieId = movieId, language = language)
            Result.success(detail)
        } catch (e: Exception) {
            val fallbackDetail = getFallbackDetail(movieId, language)
            if (fallbackDetail != null) {
                Result.success(fallbackDetail)
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun searchMovies(query: String, language: String = "en-US"): Result<List<Movie>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.searchMovies(query = query, language = language)
            Result.success(response.results)
        } catch (e: Exception) {
            val fallback = getFallbackMovies(language).filter {
                (it.title ?: "").contains(query, ignoreCase = true)
            }
            Result.success(fallback)
        }
    }

    private fun getFallbackMovies(language: String): List<Movie> {
        val isIndo = language.startsWith("id", ignoreCase = true)
        return listOf(
            Movie(
                id = 1726,
                title = "Iron Man",
                overview = if (isIndo)
                    "Menceritakan kisah Tony Stark, seorang industrialis miliarder dan penemu jenius yang diculik dan dipaksa untuk membuat senjata yang mematikan. Dengan kecerdasannya, ia membangun baju zirah berteknologi tinggi."
                else
                    "After being held captive in an Afghan cave, billionaire engineer Tony Stark creates a unique weaponized suit of armor to fight evil.",
                posterPath = "/78lPtwv72eTNqFW9COBYI0dWDJa.jpg",
                backdropPath = "/cyecB7godJ6kNHGONvOHVJz9L50.jpg",
                voteAverage = 7.7,
                voteCount = 25000,
                releaseDate = "2008-05-02"
            ),
            Movie(
                id = 155,
                title = "The Dark Knight",
                overview = if (isIndo)
                    "Ketika ancaman yang dikenal sebagai Joker mendatangkan malapetaka di Gotham City, Batman harus menerima salah satu ujian psikologis dan fisik terbesarnya."
                else
                    "Batman raises the stakes in his war on crime. With the help of allies Lt. Jim Gordon and DA Harvey Dent, Batman sets out to dismantle the remaining criminal organizations that plague the streets.",
                posterPath = "/qJ2tW6WMUDux911r6m7haRef0WH.jpg",
                backdropPath = "/nMKdUUepR0i5zn0y1T4CsSB5chy.jpg",
                voteAverage = 8.5,
                voteCount = 31000,
                releaseDate = "2008-07-18"
            ),
            Movie(
                id = 299536,
                title = "Avengers: Infinity War",
                overview = if (isIndo)
                    "Sebagai Avengers dan sekutu mereka terus melindungi dunia dari ancaman yang terlalu besar untuk ditangani oleh pahlawan manapun, bahaya baru muncul dari bayang-bayang kosmik: Thanos."
                else
                    "As the Avengers and their allies have continued to protect the world from threats too large for any one hero to handle, a new danger has emerged from the cosmic shadows: Thanos.",
                posterPath = "/7WsyChQLEftFiDOVTGkv3hFpyyt.jpg",
                backdropPath = "/mDfJG3LC3Dqb67AZ52x3Z0jU0uB.jpg",
                voteAverage = 8.3,
                voteCount = 29000,
                releaseDate = "2018-04-27"
            ),
            Movie(
                id = 157336,
                title = "Interstellar",
                overview = if (isIndo)
                    "Sekelompok penjelajah memanfaatkan lubang cacing yang baru ditemukan untuk melampaui batas perjalanan ruang angkasa manusia dan menaklukkan jarak yang sangat luas dalam pelayaran antarbintang."
                else
                    "The adventures of a group of explorers who make use of a newly discovered wormhole to surpass the limitations on human space travel and conquer the vast distances involved in an interstellar voyage.",
                posterPath = "/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
                backdropPath = "/xJHokMbljvjADYdit5fK5VQsXEG.jpg",
                voteAverage = 8.4,
                voteCount = 34000,
                releaseDate = "2014-11-07"
            )
        )
    }

    private fun getFallbackDetail(movieId: Int, language: String): MovieDetail? {
        val movies = getFallbackMovies(language)
        val movie = movies.find { it.id == movieId } ?: movies.firstOrNull() ?: return null
        return MovieDetail(
            id = movie.id,
            title = movie.title,
            originalTitle = movie.originalTitle,
            overview = movie.overview,
            posterPath = movie.posterPath,
            backdropPath = movie.backdropPath,
            voteAverage = movie.voteAverage,
            voteCount = movie.voteCount,
            releaseDate = movie.releaseDate,
            runtime = 126,
            genres = listOf(
                Genre(id = 28, name = "Action"),
                Genre(id = 878, name = "Sci-Fi"),
                Genre(id = 12, name = "Adventure")
            )
        )
    }
}
