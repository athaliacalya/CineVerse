package com.athalia.cineverse.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.athalia.cineverse.ui.theme.CineVerseTheme
import com.athalia.cineverse.ui.theme.PrimaryPink

@Composable
fun GenreChip(
    name: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .border(
                width = 1.2.dp,
                color = PrimaryPink,
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 16.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            ),
            color = PrimaryPink
        )
    }
}

@Preview(name = "Genre Chip", showBackground = true)
@Composable
fun GenreChipPreview() {
    CineVerseTheme {
        Surface {
            Box(modifier = Modifier.padding(16.dp)) {
                GenreChip(name = "Action")
            }
        }
    }
}
