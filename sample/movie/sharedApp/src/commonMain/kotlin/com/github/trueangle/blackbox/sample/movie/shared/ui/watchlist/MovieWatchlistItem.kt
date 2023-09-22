package com.github.trueangle.blackbox.sample.movie.shared.ui.watchlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.truangle.blackbox.design.typography
import com.github.trueangle.blackbox.sample.movie.shared.domain.model.Movie
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
internal fun MovieWatchlistItem(
    movie: Movie,
    onMovieSelected: () -> Unit,
    onRemoveFromWatchlist: () -> Unit
) {
    Box(modifier = Modifier.clickable(onClick = onMovieSelected)) {
        KamelImage(
            resource = asyncPainterResource(
                data = "https://image.tmdb.org/t/p/original/${movie.backdrop_path}"
            ),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(//overlay
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(Color.Black.copy(alpha = 0.1f))
        )
        Text(
            text = movie.title.orEmpty(),
            style = typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp)
        )
        IconButton(
            onClick = { onRemoveFromWatchlist.invoke() },
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        }
    }
}
