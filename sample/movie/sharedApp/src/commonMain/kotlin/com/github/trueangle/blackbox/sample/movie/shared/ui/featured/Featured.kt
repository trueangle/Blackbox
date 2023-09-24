package com.github.trueangle.blackbox.sample.movie.shared.ui.featured

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.trueangle.blackbox.core.IO
import com.github.trueangle.blackbox.multiplatform.ViewModel
import com.github.trueangle.blackbox.multiplatform.ViewModelScope
import com.github.trueangle.blackbox.multiplatform.rememberScope
import com.github.trueangle.blackbox.multiplatform.rememberViewModel
import com.github.trueangle.blackbox.sample.movie.shared.domain.model.Genre
import com.github.trueangle.blackbox.sample.movie.shared.domain.model.Movie
import com.github.trueangle.blackbox.sample.movie.shared.domain.repository.GenreRepository
import com.github.trueangle.blackbox.sample.movie.shared.domain.repository.MovieRepository
import com.github.trueangle.blackbox.sample.movie.shared.ui.widget.carousel.Pager
import com.github.trueangle.blackbox.sample.movie.shared.ui.widget.carousel.PagerState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.abs

class FeaturedDependencies(
    val movieRepository: MovieRepository,
    val genreRepository: GenreRepository
)

sealed interface FeaturedOutput {
    data class OnMovieClick(val movie: Movie) : FeaturedOutput
    data class OnBuyTicketClick(val movie: Movie) : FeaturedOutput
}

class FeaturedIO : IO<Nothing, FeaturedOutput>()

private data class MoviesState(
    val movies: ImmutableList<Movie> = persistentListOf(),
    val genres: ImmutableList<Genre> = persistentListOf(),
    val error: Boolean = false
)

private class FeaturedViewModel(
    private val repository: MovieRepository,
    private val genreRepository: GenreRepository,
    private val io: FeaturedIO
) : ViewModel() {

    private val screenStateFlow = MutableStateFlow(MoviesState())
    val screenState = screenStateFlow.asStateFlow()

    init {
        loadData()
    }

    fun onMovieClick(movie: Movie) {
        coroutineScope.launch {
            io.output(FeaturedOutput.OnMovieClick(movie))
        }
    }

    fun onBuyTicketClick(movie: Movie) {
        coroutineScope.launch {
            io.output(FeaturedOutput.OnBuyTicketClick(movie))
        }
    }

    private fun loadData() {
        coroutineScope.launch(Dispatchers.IO) {
            runCatching {
                val movies = async { repository.getMovies(1) }
                val genres = async { genreRepository.getGenres() }

                movies.await() to genres.await()
            }.onSuccess {
                screenStateFlow.emit(
                    screenStateFlow.value.copy(
                        movies = it.first.toPersistentList(),
                        genres = it.second.toPersistentList()
                    )
                )
            }.onFailure {
                it.printStackTrace()
                screenStateFlow.emit(
                    screenStateFlow.value.copy(
                        error = true
                    )
                )
            }
        }
    }
}

@Composable
fun Featured(
    modifier: Modifier,
    dependencies: FeaturedDependencies,
    io: FeaturedIO
) {
    val viewModel = rememberViewModel(key = "FeaturedScope") {
        FeaturedViewModel(
            repository = dependencies.movieRepository,
            genreRepository = dependencies.genreRepository,
            io = io
        )
    }

    val state by viewModel.screenState.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item { Spacer(modifier = Modifier.height(46.dp)) }
        item {
            Text(
                text = "Now Showing",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                modifier = Modifier.padding(16.dp)
            )
        }
        item { FeaturedMoviesPager(state, viewModel::onMovieClick, viewModel::onBuyTicketClick) }
    }
}

@Composable
private fun FeaturedMoviesPager(
    state: MoviesState,
    onMovieClick: (Movie) -> Unit,
    onBuyTicketClick: (Movie) -> Unit
) {
    val movies = state.movies

    if (movies.isNotEmpty()) {
        val pagerState = remember { PagerState(maxPage = movies.size - 1) }

        Pager(state = pagerState, modifier = Modifier.height(645.dp)) {
            val movie = movies[commingPage]

            val isSelected = pagerState.currentPage == commingPage

            val filteredOffset = if (abs(pagerState.currentPage - commingPage) < 2) {
                currentPageOffset
            } else 0f

            MoviePagerItem(
                movie,
                state.genres,
                isSelected,
                filteredOffset,
                { },
                onMovieClick,
                onBuyTicketClick
            )
        }
    } else {
        if (!state.error) {
            CircularProgressIndicator(
                modifier = Modifier.padding(24.dp)
            )
        } else {
            Text(
                text = "Unknown error",
                modifier = Modifier,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}