package io.github.trueangle.blackbox.sample.movie.shared.ui.trending

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.github.trueangle.blackbox.sample.movie.design.typography
import io.github.trueangle.blackbox.sample.movie.shared.domain.model.Movie
import com.kmpalette.DominantColorState
import com.kmpalette.loader.rememberNetworkLoader
import com.kmpalette.rememberDominantColorState
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun TrendingSection(
    sectionState: TrendingSectionState,
    title: String = "",
    onMovieSelected: (Movie, DominantColorState<Url>) -> Unit
) {
    if (title.isNotEmpty()) {
        Text(
            text = title,
            style = typography.titleLarge,
            modifier = Modifier.padding(start = 16.dp, end = 8.dp, bottom = 8.dp, top = 24.dp)
        )
    }
    when {
        sectionState.data.isNotEmpty() -> Content(
            sectionState.data,
            onMovieSelected
        )

        else -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                if (sectionState.error) {
                    Text("Error loading data")
                } else {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun Content(
    movies: ImmutableList<Movie>,
    onMovieSelected: (Movie, DominantColorState<Url>) -> Unit
) {
    LazyRow {
        items(
            items = movies,
            itemContent = { movie: Movie ->

                val networkLoader = rememberNetworkLoader()
                val dominantColorState =
                    rememberDominantColorState(
                        loader = networkLoader,
                        defaultColor = MaterialTheme.colorScheme.background,
                        defaultOnColor = MaterialTheme.colorScheme.onBackground
                    )

                val posterUrl = "https://image.tmdb.org/t/p/w500/${movie.poster_path}"
                LaunchedEffect(Unit) {
                    dominantColorState.updateFrom(Url(posterUrl))
                }

                KamelImage(
                    resource = asyncPainterResource(
                        data = posterUrl,
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .width(190.dp)
                        .height(300.dp)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = { onMovieSelected(movie, dominantColorState) }),
                    contentScale = ContentScale.Crop
                )
            })
    }
}