package com.rafiur.assessmentproject.omdb.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.rafiur.assessmentproject.omdb.domain.models.Movie

@Composable
fun MovieDetailsDialog(
    movie: Movie,
    onCloseClicked: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCloseClicked,
        title = { Text(text = movie.toString()) },
        text = {
            Box(
                modifier = Modifier
                    .size(width = 400.dp, height = 200.dp) // Set custom width and height
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                val painter =
                    rememberAsyncImagePainter(model = movie.poster)
                Image(
                    painter = painter,
                    contentDescription = "Forest Image",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }
        },
        confirmButton = {
            Button(onClick = onCloseClicked) {
                Text(text = "CLOSE")
            }
        }
    )
}