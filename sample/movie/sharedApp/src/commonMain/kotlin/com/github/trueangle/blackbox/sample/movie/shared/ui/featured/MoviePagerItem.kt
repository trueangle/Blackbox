package com.github.trueangle.blackbox.sample.movie.shared.ui.featured

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.truangle.blackbox.design.typography
import com.github.trueangle.blackbox.sample.movie.shared.domain.model.Genre
import com.github.trueangle.blackbox.sample.movie.shared.domain.model.Movie
import com.github.trueangle.blackbox.sample.movie.shared.ui.widget.InterestTag
import com.kmpalette.DominantColorState
import com.kmpalette.loader.rememberNetworkLoader
import com.kmpalette.rememberDominantColorState
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url
import kotlin.math.abs
import kotlin.math.min

@Composable
fun MoviePagerItem(
    movie: Movie,
    genres: List<Genre>,
    isSelected: Boolean,
    offset: Float,
    onItemClick: (Movie, DominantColorState<Url>) -> Unit,
    onBuyTicketClick: (Movie) -> Unit
) {
    val animateHeight = getOffsetBasedValue(
        selectedValue = 645,
        nonSelectedValue = 360,
        isSelected = isSelected,
        offset = offset
    ).dp
    val animateWidth = getOffsetBasedValue(
        selectedValue = 340,
        nonSelectedValue = 320,
        isSelected = isSelected,
        offset = offset
    ).dp
    val animateElevation = getOffsetBasedValue(
        selectedValue = 6,
        nonSelectedValue = 6,
        isSelected = isSelected,
        offset = offset
    ).dp

    val posterFullPath = "https://image.tmdb.org/t/p/w500/${movie.poster_path}"

    val movieGenres by remember {
        derivedStateOf {
            movie.genre_ids?.let { movieGenreIds ->
                genres.filter { movieGenreIds.contains(it.id) }.take(3)
            }
        }
    }

    val networkLoader = rememberNetworkLoader()
    val dominantColorState =
        rememberDominantColorState(
            loader = networkLoader,
            defaultColor = MaterialTheme.colorScheme.background,
            defaultOnColor = MaterialTheme.colorScheme.onBackground
        )

    LaunchedEffect(Unit) {
        dominantColorState.updateFrom(Url("https://image.tmdb.org/t/p/w500/${movie.poster_path}"))
    }

    Card(
        elevation = cardElevation(animateDpAsState(animateElevation).value),
        modifier = Modifier
            .width(animateWidth)
            .height(animateHeight)
            .padding(top = 0.dp, start = 24.dp, end = 24.dp, bottom = 0.dp)
            .clickable {
                onItemClick(movie, dominantColorState)
            },
        shape = RoundedCornerShape(16.dp),
        colors = cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        )
    ) {
        Column {
            KamelImage(
                resource = asyncPainterResource(data = posterFullPath),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = movie.title.orEmpty(),
                    modifier = Modifier.padding(8.dp),
                    style = typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                )
            }

            movieGenres?.let { genres ->
                LazyRow() {
                    items(genres) {
                        InterestTag(text = it.name)
                    }
                }
            }

            Row {
                Text(
                    text = "Release: ${movie.release_date}",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = typography.titleLarge.copy(fontSize = 12.sp)
                )
                Text(
                    text = "PG13  •  ${movie.vote_average}/10",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = typography.titleLarge.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Text(
                text = movie.overview,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
                    .weight(1f),
                style = typography.titleSmall
            )
            Button(
                onClick = {
                    onBuyTicketClick(movie)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                )
            ) {
                Text(text = "Buy Tickets", modifier = Modifier.padding(8.dp))
            }
        }
    }
}

private fun getOffsetBasedValue(
    selectedValue: Int,
    nonSelectedValue: Int,
    isSelected: Boolean,
    offset: Float,
): Float {
    val actualOffset = if (isSelected) 1 - abs(offset) else abs(offset)
    val delta = abs(selectedValue - nonSelectedValue)
    val offsetBasedDelta = delta * actualOffset

    return min(selectedValue, nonSelectedValue) + offsetBasedDelta
}
