package com.github.trueangle.blackbox.sample.movie.shared.ui.detail

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.truangle.blackbox.design.typography
import com.github.trueangle.blackbox.multiplatform.ViewModel
import com.github.trueangle.blackbox.multiplatform.ViewModelScope
import com.github.trueangle.blackbox.multiplatform.rememberScope
import com.github.trueangle.blackbox.sample.movie.shared.domain.interactor.MovieDetailsInteractor
import com.github.trueangle.blackbox.sample.movie.shared.domain.interactor.MovieDetailsInteractorImpl
import com.github.trueangle.blackbox.sample.movie.shared.domain.model.Genre
import com.github.trueangle.blackbox.sample.movie.shared.domain.model.Movie
import com.github.trueangle.blackbox.sample.movie.shared.domain.model.MovieDetails
import com.github.trueangle.blackbox.sample.movie.shared.domain.repository.GenreRepository
import com.github.trueangle.blackbox.sample.movie.shared.domain.repository.MovieRepository
import com.github.trueangle.blackbox.sample.movie.shared.ui.widget.InterestTag
import io.kamel.core.isSuccess
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Immutable
internal class MovieDetailsDependencies(
    val movieRepository: MovieRepository,
    val genreRepository: GenreRepository
)

private data class MovieDetailsState(
    val error: Boolean = false,
    val details: MovieDetails? = null,
    val genres: ImmutableList<Genre> = persistentListOf(),
    val similarMovies: ImmutableList<Movie> = persistentListOf()
)

private class MovieDetailsViewModel(
    private val movieId: String,
    private val interactor: MovieDetailsInteractor
) : ViewModel() {
    val state = MutableStateFlow(
        MovieDetailsState()
    )

    init {
        coroutineScope.launch {
            runCatching {
                interactor.getDetailsByMovieId(movieId)
            }.onSuccess { result ->
                val oldState = state.value

                state.emit(
                    oldState.copy(
                        details = result.movie,
                        genres = result.genres,
                        similarMovies = result.similarMovies
                    )
                )
            }.onFailure {
                it.printStackTrace()
                state.emit(state.value.copy(error = true))
            }
        }
    }
}

private fun createScope(
    movieId: String,
    dependencies: MovieDetailsDependencies
) = ViewModelScope {
    val interactor = MovieDetailsInteractorImpl(
        movieRepository = dependencies.movieRepository,
        genreRepository = dependencies.genreRepository
    )

    MovieDetailsViewModel(movieId, interactor)
}

@Composable
internal fun MovieDetails(
    modifier: Modifier = Modifier,
    movieId: String,
    dependencies: MovieDetailsDependencies
) {
    /*  var dominantColors = listOf(graySurface, Color.Black)

  if (imageId != 0) {
      val context = LocalContext.current
      val currentBitmap = ImageBitmap.imageResource(context.resources, imageId)

      val swatch = currentBitmap.asAndroidBitmap().generateDominantColorState()
      dominantColors = listOf(Color(swatch.rgb), Color.Black)
  }
*/

    val scope =
        rememberScope(key = "MovieDetailsScope") { createScope(movieId, dependencies) }

    val expand = remember { mutableStateOf(false) }

    val state by scope.viewModel.state.collectAsState()
    val details = state.details
    val similarMovies = state.similarMovies

    LazyColumn(
        modifier = modifier.background(Color.Black)
            //.verticalGradientBackground(dominantColors)
            .padding(
                animateDpAsState(
                    if (expand.value) 0.dp else 120.dp,
                    tween(250)
                ).value
            )
    ) {
        when {
            state.error -> item { Text(text = "Error loading data", color = Color.White) }
            state.details != null -> {
                item {
                    val painter = asyncPainterResource(
                        data = "https://image.tmdb.org/t/p/w500/${details!!.posterPath}"
                    )

                    KamelImage(
                        resource = painter,
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier
                            .height(
                                600.dp
                            )
                            .fillMaxWidth(),
                    )
                    when {
                        painter.isSuccess -> expand.value = true
                        else -> expand.value = false
                    }
                }
                item {
                    Column(modifier = Modifier.background(MaterialTheme.colorScheme.onSurface)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = details!!.title,
                                modifier = Modifier.padding(8.dp),
                                style = typography.titleLarge,
                                color = Color.White
                            )
                            IconButton(onClick = {}) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Row {
                            details!!.genres.forEach {
                                InterestTag(text = it.name)
                            }
                        }

                        Text(
                            text = "Release: ${details!!.releaseDate}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = typography.titleLarge.copy(fontSize = 12.sp),
                            color = Color.White
                        )
                        Text(
                            text = "PG13  •  ${details.voteAverage}/10",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = typography.titleLarge.copy(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        )
                        Text(
                            text = details.overview.orEmpty(),
                            modifier = Modifier
                                .padding(8.dp),
                            style = typography.titleSmall,
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        SimilarMoviesSection(similarMovies)

                        Spacer(modifier = Modifier.height(50.dp))

                        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Get Tickets", modifier = Modifier.padding(8.dp))
                        }
                    }
                }
            }

            else -> item { CircularProgressIndicator(Modifier.size(24.dp)) }
        }
    }
}

@Composable
internal fun SimilarMoviesSection(similarMovies: ImmutableList<Movie>) {
    Text(
        text = "Similar Movies",
        style = typography.titleLarge,
        modifier = Modifier.padding(8.dp),
        color = Color.Gray
    )
    LazyRow {
        items(
            items = similarMovies,
            itemContent = { movie: Movie ->
                KamelImage(
                    resource = asyncPainterResource(
                        data = "https://image.tmdb.org/t/p/w500/${movie.poster_path}"
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .width(200.dp)
                        .height(300.dp)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        )
    }
}

