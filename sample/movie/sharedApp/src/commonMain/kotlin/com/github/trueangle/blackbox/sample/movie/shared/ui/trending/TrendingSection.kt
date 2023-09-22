package com.github.trueangle.blackbox.sample.movie.shared.ui.trending

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.github.truangle.blackbox.design.typography
import com.github.trueangle.blackbox.sample.movie.shared.domain.model.Movie
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun TrendingSection(
    sectionState: TrendingSectionState,
    title: String = "",
    onMovieSelected: (Movie) -> Unit
) {
    when {
        sectionState.error -> {
            Box {
                Text("Error loading data")
            }
        }

        sectionState.data.isNotEmpty() -> Content(
            sectionState.data,
            title,
            onMovieSelected
        )

        else -> CircularProgressIndicator()
    }
}

@Composable
private fun Content(
    movies: ImmutableList<Movie>,
    title: String = "",
    onMovieSelected: (Movie) -> Unit
) {
    if (title.isNotEmpty()) {
        Text(
            text = title,
            style = typography.titleLarge,
            modifier = Modifier.padding(start = 16.dp, end = 8.dp, bottom = 8.dp, top = 24.dp)
        )
    }
    LazyRow {
        items(
            items = movies,
            itemContent = { movie: Movie ->
                KamelImage(
                    resource = asyncPainterResource(
                        data = "https://image.tmdb.org/t/p/w500/${movie.poster_path}",
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .width(190.dp)
                        .height(300.dp)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = { onMovieSelected(movie) }),
                    contentScale = ContentScale.Crop
                )
            })
    }
}