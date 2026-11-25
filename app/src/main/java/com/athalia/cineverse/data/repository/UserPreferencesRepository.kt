package com.athalia.cineverse.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "cineverse_preferences")

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

enum class AppLanguage(val code: String, val apiLanguage: String) {
    INDONESIAN("in", "id-ID"),
    ENGLISH("en", "en-US")
}

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val LANGUAGE = stringPreferencesKey("app_language")
    }

    val themeModeFlow: Flow<ThemeMode> = context.dataStore.data.map { preferences ->
        when (preferences[PreferencesKeys.THEME_MODE]) {
            ThemeMode.LIGHT.name -> ThemeMode.LIGHT
            ThemeMode.DARK.name -> ThemeMode.DARK
            else -> ThemeMode.SYSTEM
        }
    }

    val languageFlow: Flow<AppLanguage> = context.dataStore.data.map { preferences ->
        when (preferences[PreferencesKeys.LANGUAGE]) {
            AppLanguage.INDONESIAN.name -> AppLanguage.INDONESIAN
            else -> AppLanguage.ENGLISH
        }
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = mode.name
        }
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE] = language.name
        }
    }
}
