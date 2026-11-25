package com.athalia.cineverse.data.model

import com.google.gson.annotations.SerializedName
import java.util.Locale

data class MovieResponse(
    @SerializedName("page") val page: Int = 1,
    @SerializedName("results") val results: List<Movie> = emptyList(),
    @SerializedName("total_pages") val totalPages: Int = 1,
    @SerializedName("total_results") val totalResults: Int = 0
)

data class Movie(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String? = null,
    @SerializedName("original_title") val originalTitle: String? = null,
    @SerializedName("overview") val overview: String? = null,
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("vote_count") val voteCount: Int = 0,
    @SerializedName("release_date") val releaseDate: String? = null,
    @SerializedName("genre_ids") val genreIds: List<Int> = emptyList()
) {
    val displayTitle: String get() = title ?: originalTitle ?: ""
    val formattedRating: String get() = String.format(Locale.US, "%.1f", voteAverage)
    val releaseYear: String get() = releaseDate?.take(4) ?: ""
    val fullPosterUrl: String? get() = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
    val fullBackdropUrl: String? get() = backdropPath?.let { "https://image.tmdb.org/t/p/w780$it" }
}

data class MovieDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String? = null,
    @SerializedName("original_title") val originalTitle: String? = null,
    @SerializedName("overview") val overview: String? = null,
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("vote_count") val voteCount: Int = 0,
    @SerializedName("release_date") val releaseDate: String? = null,
    @SerializedName("runtime") val runtime: Int? = null,
    @SerializedName("genres") val genres: List<Genre> = emptyList()
) {
    val displayTitle: String get() = title ?: originalTitle ?: ""
    val formattedRating: String get() = String.format(Locale.US, "%.1f", voteAverage)
    val releaseYear: String get() = releaseDate?.take(4) ?: ""
    val fullPosterUrl: String? get() = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
    val fullBackdropUrl: String? get() = backdropPath?.let { "https://image.tmdb.org/t/p/w780$it" }
}

data class Genre(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)
