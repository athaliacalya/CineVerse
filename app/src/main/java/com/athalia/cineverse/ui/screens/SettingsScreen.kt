package com.athalia.cineverse.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.athalia.cineverse.R
import com.athalia.cineverse.data.repository.AppLanguage
import com.athalia.cineverse.data.repository.ThemeMode
import com.athalia.cineverse.ui.theme.CineVerseTheme
import com.athalia.cineverse.ui.theme.PrimaryPink
import com.athalia.cineverse.ui.theme.ToggleBackgroundDark
import com.athalia.cineverse.ui.theme.ToggleBackgroundLight
import com.athalia.cineverse.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentTheme by viewModel.themeMode.collectAsState()
    val currentLanguage by viewModel.language.collectAsState()

    SettingsContent(
        currentTheme = currentTheme,
        currentLanguage = currentLanguage,
        onThemeSelect = { viewModel.setThemeMode(it) },
        onLanguageSelect = { viewModel.setLanguage(it) },
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    currentTheme: ThemeMode,
    currentLanguage: AppLanguage,
    onThemeSelect: (ThemeMode) -> Unit,
    onLanguageSelect: (AppLanguage) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Language Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = stringResource(R.string.language),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stringResource(R.string.language_desc),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    val toggleBg = if (currentTheme == ThemeMode.DARK || (currentTheme == ThemeMode.SYSTEM && isDark)) {
                        ToggleBackgroundDark
                    } else {
                        ToggleBackgroundLight
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(toggleBg)
                            .padding(4.dp)
                    ) {
                        SegmentedOption(
                            text = stringResource(R.string.indonesian),
                            isSelected = currentLanguage == AppLanguage.INDONESIAN,
                            onClick = { onLanguageSelect(AppLanguage.INDONESIAN) },
                            modifier = Modifier.weight(1f)
                        )
                        SegmentedOption(
                            text = stringResource(R.string.english),
                            isSelected = currentLanguage == AppLanguage.ENGLISH,
                            onClick = { onLanguageSelect(AppLanguage.ENGLISH) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Theme Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = stringResource(R.string.theme),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stringResource(R.string.theme_desc),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    val toggleBg = if (currentTheme == ThemeMode.DARK || (currentTheme == ThemeMode.SYSTEM && isDark)) {
                        ToggleBackgroundDark
                    } else {
                        ToggleBackgroundLight
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(toggleBg)
                            .padding(4.dp)
                    ) {
                        SegmentedOption(
                            text = stringResource(R.string.theme_light),
                            isSelected = currentTheme == ThemeMode.LIGHT,
                            onClick = { onThemeSelect(ThemeMode.LIGHT) },
                            modifier = Modifier.weight(1f)
                        )
                        SegmentedOption(
                            text = stringResource(R.string.theme_dark),
                            isSelected = currentTheme == ThemeMode.DARK,
                            onClick = { onThemeSelect(ThemeMode.DARK) },
                            modifier = Modifier.weight(1f)
                        )
                        SegmentedOption(
                            text = stringResource(R.string.theme_system),
                            isSelected = currentTheme == ThemeMode.SYSTEM,
                            onClick = { onThemeSelect(ThemeMode.SYSTEM) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // About Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = stringResource(R.string.about),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = stringResource(R.string.about_desc),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun SegmentedOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) PrimaryPink else Color.Transparent,
        label = "segment_bg"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "segment_text"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(9.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            ),
            color = textColor
        )
    }
}

@Preview(name = "Settings - Light", showBackground = true)
@Composable
fun SettingsScreenLightPreview() {
    CineVerseTheme(darkTheme = false) {
        Surface {
            SettingsContent(
                currentTheme = ThemeMode.LIGHT,
                currentLanguage = AppLanguage.INDONESIAN,
                onThemeSelect = {},
                onLanguageSelect = {},
                onBackClick = {}
            )
        }
    }
}

@Preview(name = "Settings - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun SettingsScreenDarkPreview() {
    CineVerseTheme(darkTheme = true) {
        Surface {
            SettingsContent(
                currentTheme = ThemeMode.DARK,
                currentLanguage = AppLanguage.INDONESIAN,
                onThemeSelect = {},
                onLanguageSelect = {},
                onBackClick = {}
            )
        }
    }
}
