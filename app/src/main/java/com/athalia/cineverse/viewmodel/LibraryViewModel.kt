package com.athalia.cineverse.viewmodel

import androidx.lifecycle.ViewModel
import com.athalia.cineverse.data.model.Movie
import com.athalia.cineverse.data.model.SavedMovie
import com.athalia.cineverse.data.repository.LibraryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LibraryViewModel(private val repository: LibraryRepository) : ViewModel() {
    private val _items = MutableStateFlow(repository.load())
    val items: StateFlow<List<SavedMovie>> = _items.asStateFlow()

    private fun update(movie: Movie, change: (SavedMovie) -> SavedMovie) {
        val current = _items.value
        val old = current.find { it.movie.id == movie.id } ?: SavedMovie(movie)
        val new = change(old)
        val rest = current.filter { it.movie.id != movie.id }
        val empty = !new.watchlist && !new.watched && new.rating == 0f && new.review.isBlank()
        val result = if (empty) rest else listOf(new) + rest
        _items.value = result
        repository.save(result)
    }

    fun toggleWatchlist(m: Movie) = update(m) { it.copy(watchlist = !it.watchlist) }
    fun toggleWatched(m: Movie) = update(m) { it.copy(watched = !it.watched) }
    fun setRating(m: Movie, r: Float) =
        update(m) { it.copy(rating = if (it.rating == r) 0f else r, watched = true) }
    fun setReview(m: Movie, text: String) = update(m) { it.copy(review = text.trim()) }
}
