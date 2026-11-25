package com.athalia.cineverse.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.athalia.cineverse.data.repository.MovieRepository
import com.athalia.cineverse.data.repository.UserPreferencesRepository

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    private val preferencesRepository by lazy { UserPreferencesRepository(context.applicationContext) }
    private val movieRepository by lazy { MovieRepository() }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MovieViewModel::class.java) -> {
                MovieViewModel(movieRepository, preferencesRepository) as T
            }
            modelClass.isAssignableFrom(MovieDetailViewModel::class.java) -> {
                MovieDetailViewModel(movieRepository, preferencesRepository) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(preferencesRepository) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(movieRepository, preferencesRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
