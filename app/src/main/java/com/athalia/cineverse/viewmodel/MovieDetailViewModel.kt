package com.athalia.cineverse.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athalia.cineverse.data.model.MovieDetail
import com.athalia.cineverse.data.repository.AppLanguage
import com.athalia.cineverse.data.repository.MovieRepository
import com.athalia.cineverse.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MovieDetailUiState(
    val isLoading: Boolean = false,
    val movieDetail: MovieDetail? = null,
    val errorMessage: String? = null
)

class MovieDetailViewModel(
    private val repository: MovieRepository = MovieRepository(),
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    private var currentMovieId: Int? = null

    fun loadDetail(movieId: Int) {
        currentMovieId = movieId
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val currentLanguage = preferencesRepository.languageFlow.first()
            val result = repository.getMovieDetail(movieId, currentLanguage.apiLanguage)
            result.onSuccess { detail ->
                _uiState.update {
                    it.copy(isLoading = false, movieDetail = detail, errorMessage = null)
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = error.localizedMessage ?: "Failed to load details")
                }
            }
        }
    }

    fun retry() {
        currentMovieId?.let { loadDetail(it) }
    }
}
