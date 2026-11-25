package com.athalia.cineverse.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athalia.cineverse.data.model.Movie
import com.athalia.cineverse.data.repository.AppLanguage
import com.athalia.cineverse.data.repository.MovieRepository
import com.athalia.cineverse.data.repository.UserPreferencesRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DashboardUiState(
    val isLoading: Boolean = false,
    val trendingMovie: Movie? = null,
    val popularMovies: List<Movie> = emptyList(),
    val nowPlayingMovies: List<Movie> = emptyList(),
    val topRatedMovies: List<Movie> = emptyList(),
    val errorMessage: String? = null
)

class MovieViewModel(
    private val repository: MovieRepository = MovieRepository(),
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState(isLoading = true))
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private var currentLanguage: AppLanguage = AppLanguage.ENGLISH

    init {
        viewModelScope.launch {
            preferencesRepository.languageFlow.collectLatest { lang ->
                currentLanguage = lang
                loadAllMovies(lang.apiLanguage)
            }
        }
    }

    fun retry() {
        loadAllMovies(currentLanguage.apiLanguage)
    }

    fun loadAllMovies(apiLanguage: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val trendingDeferred = async { repository.getTrendingMovies(apiLanguage) }
            val popularDeferred = async { repository.getPopularMovies(apiLanguage) }
            val nowPlayingDeferred = async { repository.getNowPlayingMovies(apiLanguage) }
            val topRatedDeferred = async { repository.getTopRatedMovies(apiLanguage) }

            val trendingResult = trendingDeferred.await()
            val popularResult = popularDeferred.await()
            val nowPlayingResult = nowPlayingDeferred.await()
            val topRatedResult = topRatedDeferred.await()

            val hasSuccess = trendingResult.isSuccess || popularResult.isSuccess ||
                    nowPlayingResult.isSuccess || topRatedResult.isSuccess

            if (hasSuccess) {
                val trendingList = trendingResult.getOrDefault(emptyList())
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        trendingMovie = trendingList.firstOrNull(),
                        popularMovies = popularResult.getOrDefault(emptyList()),
                        nowPlayingMovies = nowPlayingResult.getOrDefault(emptyList()),
                        topRatedMovies = topRatedResult.getOrDefault(emptyList()),
                        errorMessage = null
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to load movie data. Please check your internet connection."
                    )
                }
            }
        }
    }
}
