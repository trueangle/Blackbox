package io.github.trueangle.blackbox.sample.movie.shared.ui.detail

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import io.github.trueangle.blackbox.sample.movie.design.typography
import io.github.trueangle.blackbox.core.IO
import io.github.trueangle.blackbox.multiplatform.ViewModel
import io.github.trueangle.blackbox.multiplatform.ViewModelScope
import io.github.trueangle.blackbox.multiplatform.rememberScope
import io.github.trueangle.blackbox.sample.movie.design.MainButton
import io.github.trueangle.blackbox.sample.movie.shared.domain.interactor.MovieDetailsInteractor
import io.github.trueangle.blackbox.sample.movie.shared.domain.interactor.MovieDetailsInteractorImpl
import io.github.trueangle.blackbox.sample.movie.shared.domain.model.Genre
import io.github.trueangle.blackbox.sample.movie.shared.domain.model.Movie
import io.github.trueangle.blackbox.sample.movie.shared.domain.model.MovieDetails
import io.github.trueangle.blackbox.sample.movie.shared.domain.repository.GenreRepository
import io.github.trueangle.blackbox.sample.movie.shared.domain.repository.MovieRepository
import io.github.trueangle.blackbox.sample.movie.shared.ui.widget.InterestTag
import io.kamel.core.isSuccess
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Immutable
internal data class MovieDetailsConfig(
    val movieId: String,
    val movieName: String,
    val dominantColor: String,
    val dominantOnColor: String
)

@Immutable
internal class MovieDetailsDependencies(
    val movieRepository: MovieRepository,
    val genreRepository: GenreRepository
)

sealed interface MovieDetailsOutput {
    data object OnClose : MovieDetailsOutput
    data class OnRequestTickets(val movieName: String) : MovieDetailsOutput
}

class MovieDetailsIO : IO<Nothing, MovieDetailsOutput>()

@Composable
internal fun MovieDetails(
    modifier: Modifier = Modifier,
    config: MovieDetailsConfig,
    dependencies: MovieDetailsDependencies,
    io: MovieDetailsIO
) {
    val scope =
        rememberScope(key = "MovieDetailsScope") { createScope(config, dependencies, io) }
    val vm = scope.viewModel

    val state by vm.state.collectAsState()

    if (state.details != null) {
        Content(modifier, state, config, vm::onCloseClick, vm::onRequestTickets)
    } else {
        Box(
            modifier.background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            if (state.error) {
                Text(text = "Error loading data", color = Color.White)
            } else {
                CircularProgressIndicator(Modifier.size(24.dp))
            }
        }
    }
}

private fun createScope(
    config: MovieDetailsConfig,
    dependencies: MovieDetailsDependencies,
    io: MovieDetailsIO
) = ViewModelScope {
    val interactor = MovieDetailsInteractorImpl(
        movieRepository = dependencies.movieRepository,
        genreRepository = dependencies.genreRepository
    )

    MovieDetailsViewModel(config, interactor, io)
}

private data class MovieDetailsState(
    val error: Boolean = false,
    val details: MovieDetails? = null,
    val genres: ImmutableList<Genre> = persistentListOf(),
    val similarMovies: ImmutableList<Movie> = persistentListOf()
)

private class MovieDetailsViewModel(
    private val config: MovieDetailsConfig,
    private val interactor: MovieDetailsInteractor,
    private val io: MovieDetailsIO
) : ViewModel() {

    val state = MutableStateFlow(
        MovieDetailsState()
    )

    init {
        coroutineScope.launch {
            runCatching {
                interactor.getDetailsByMovieId(config.movieId)
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

    fun onRequestTickets() {
        coroutineScope.launch { io.output(MovieDetailsOutput.OnRequestTickets(config.movieName)) }
    }

    fun onCloseClick() {
        coroutineScope.launch { io.output(MovieDetailsOutput.OnClose) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    modifier: Modifier,
    state: MovieDetailsState,
    config: MovieDetailsConfig,
    onCloseClick: () -> Unit,
    onRequestTickets: () -> Unit
) {
    val expand = remember { mutableStateOf(false) }
    val details = state.details
    val similarMovies = state.similarMovies

    val containerColor = remember { Color(config.dominantColor.toULong()) }
    val containerOnColor = remember { Color(config.dominantOnColor.toULong()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                actions = {
                    IconButton(
                        onClick = onCloseClick,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(
                                0.7F
                            )
                        ),
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(0.6F)
                        )
                    }
                })
        }
    ) {
        LazyColumn(
            modifier = modifier
                .background(containerColor)
                .padding(
                    animateDpAsState(
                        if (expand.value) 0.dp else 120.dp,
                        tween(150)
                    ).value
                )
        ) {
            item {
                val painter = asyncPainterResource(
                    data = "https://image.tmdb.org/t/p/w500/${details!!.posterPath}"
                )

                KamelImage(
                    resource = painter,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .height(600.dp)
                        .fillMaxWidth(),
                )
                when {
                    painter.isSuccess -> expand.value = true
                    else -> expand.value = false
                }
            }
            item {

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = containerColor,
                        contentColor = containerOnColor,
                    ),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Column(modifier = Modifier) {
                        Spacer(Modifier.width(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = details!!.title,
                                modifier = Modifier.padding(vertical = 8.dp),
                                style = typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            )
                        }

                        Row {
                            Spacer(Modifier.width(8.dp))
                            details!!.genres.forEach {
                                InterestTag(text = it.name)
                            }
                        }

                        Row(modifier = Modifier.padding(start = 8.dp)) {
                            Text(
                                text = "Release: ${details!!.releaseDate}",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = typography.titleLarge.copy(fontSize = 12.sp)
                            )
                            Text(
                                text = "PG13  •  ${details.voteAverage}/10",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = typography.titleLarge.copy(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }

                        Text(
                            text = details?.overview.orEmpty(),
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            style = typography.titleSmall.copy(
                                color = containerOnColor.copy(
                                    alpha = 0.6F
                                )
                            ),
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        SimilarMoviesSection(
                            similarMovies,
                            color = containerOnColor.copy(alpha = 0.6F)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        MainButton(
                            text = "Get tickets",
                            onClick = onRequestTickets,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                .fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SimilarMoviesSection(similarMovies: ImmutableList<Movie>, color: Color) {
    Text(
        text = "Similar Movies",
        style = typography.titleMedium.copy(fontSize = 18.sp, color = color),
        modifier = Modifier.padding(horizontal = 16.dp),
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

