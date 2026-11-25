package com.athalia.cineverse.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.athalia.cineverse.R
import com.athalia.cineverse.ui.theme.CineVerseTheme
import com.athalia.cineverse.ui.theme.PrimaryPink

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = PrimaryPink,
            strokeWidth = 3.dp,
            modifier = Modifier.size(44.dp)
        )
    }
}

@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = PrimaryPink,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPink),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = stringResource(R.string.retry),
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Preview(name = "Loading State", showBackground = true)
@Composable
fun LoadingStatePreview() {
    CineVerseTheme {
        Surface {
            LoadingState()
        }
    }
}

@Preview(name = "Error State - Light", showBackground = true)
@Composable
fun ErrorStateLightPreview() {
    CineVerseTheme(darkTheme = false) {
        Surface {
            ErrorState(
                message = "Gagal memuat data. Periksa koneksi internet Anda.",
                onRetry = {}
            )
        }
    }
}

@Preview(name = "Error State - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ErrorStateDarkPreview() {
    CineVerseTheme(darkTheme = true) {
        Surface {
            ErrorState(
                message = "Gagal memuat data. Periksa koneksi internet Anda.",
                onRetry = {}
            )
        }
    }
}
