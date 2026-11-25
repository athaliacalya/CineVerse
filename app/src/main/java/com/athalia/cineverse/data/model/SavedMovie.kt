package com.athalia.cineverse.data.model

data class SavedMovie(
    val movie: Movie,
    val watchlist: Boolean = false,
    val watched: Boolean = false,
    val rating: Float = 0f,      // 0 = belum dinilai, 1..5
    val review: String = ""
)

fun MovieDetail.toMovie(): Movie = Movie(
    id = id,
    title = title,
    originalTitle = originalTitle,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    voteAverage = voteAverage,
    voteCount = voteCount,
    releaseDate = releaseDate
)
