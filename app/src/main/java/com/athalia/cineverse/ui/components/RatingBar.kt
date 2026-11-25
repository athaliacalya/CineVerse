package com.athalia.cineverse.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.athalia.cineverse.ui.theme.CineVerseTheme
import com.athalia.cineverse.ui.theme.PrimaryPink

@Composable
fun RatingBar(
    rating: Float,
    onRate: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                contentDescription = "$i",
                tint = if (i <= rating) PrimaryPink else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f),
                modifier = Modifier
                    .size(38.dp)
                    .clickable { onRate(i.toFloat()) }
            )
        }
    }
}

@Preview(name = "RatingBar Preview", showBackground = true)
@Composable
fun RatingBarPreview() {
    CineVerseTheme {
        Surface {
            RatingBar(
                rating = 4f,
                onRate = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
