package com.athalia.cineverse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.athalia.cineverse.data.repository.AppLanguage
import com.athalia.cineverse.data.repository.ThemeMode
import com.athalia.cineverse.data.repository.UserPreferencesRepository
import com.athalia.cineverse.ui.navigation.AppNavGraph
import com.athalia.cineverse.ui.theme.CineVerseTheme
import com.athalia.cineverse.util.LocaleHelper
import com.athalia.cineverse.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val preferencesRepository = UserPreferencesRepository(applicationContext)
        val factory = ViewModelFactory(applicationContext)

        setContent {
            val themeMode by preferencesRepository.themeModeFlow.collectAsState(initial = ThemeMode.SYSTEM)
            val language by preferencesRepository.languageFlow.collectAsState(initial = AppLanguage.INDONESIAN)
            val isDarkSystem = isSystemInDarkTheme()

            val isDarkTheme = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isDarkSystem
            }

            val baseContext = LocalContext.current
            val localizedContext = remember(language) {
                LocaleHelper.getLocalizedContext(baseContext, language.code)
            }

            CompositionLocalProvider(
                LocalContext provides localizedContext,
                LocalConfiguration provides localizedContext.resources.configuration
            ) {
                CineVerseTheme(darkTheme = isDarkTheme) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        val navController: NavHostController = rememberNavController()
                        AppNavGraph(
                            navController = navController,
                            factory = factory
                        )
                    }
                }
            }
        }
    }
}
