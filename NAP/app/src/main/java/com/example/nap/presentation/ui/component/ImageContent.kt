package com.example.nap.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage


@Composable
fun ImageContent(
    modifier: Modifier = Modifier,
    imageUrl: String,
    onDeleteImageClick: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        AsyncImage(
            model = imageUrl,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentDescription = "Image from gallery",
            contentScale = ContentScale.Fit
        )
        Icon(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(24.dp)
                .clickable {
                    onDeleteImageClick()
                },
            imageVector = Icons.Default.Close,
            contentDescription = "Remove image",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}
