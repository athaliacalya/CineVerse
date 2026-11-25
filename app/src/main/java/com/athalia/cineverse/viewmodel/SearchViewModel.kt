package com.athalia.cineverse.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athalia.cineverse.data.model.Movie
import com.athalia.cineverse.data.repository.AppLanguage
import com.athalia.cineverse.data.repository.MovieRepository
import com.athalia.cineverse.data.repository.UserPreferencesRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val results: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class SearchViewModel(
    private val repository: MovieRepository = MovieRepository(),
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        _uiState.update { it.copy(query = newQuery) }
        searchJob?.cancel()

        if (newQuery.isBlank()) {
            _uiState.update { it.copy(results = emptyList(), isLoading = false, errorMessage = null) }
            return
        }

        searchJob = viewModelScope.launch {
            delay(400) // Debounce
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val currentLanguage = preferencesRepository.languageFlow.first()
            val result = repository.searchMovies(newQuery.trim(), currentLanguage.apiLanguage)
            result.onSuccess { movies ->
                _uiState.update {
                    it.copy(isLoading = false, results = movies, errorMessage = null)
                }
            }.onFailure { err ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = err.localizedMessage)
                }
            }
        }
    }

    fun clearSearch() {
        searchJob?.cancel()
        _uiState.update { SearchUiState() }
    }
}
