package com.github.trueangle.blackbox.sample.movie.shared.ui.trending

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.github.trueangle.blackbox.core.IO
import com.github.trueangle.blackbox.multiplatform.ViewModel
import com.github.trueangle.blackbox.multiplatform.ViewModelScope
import com.github.trueangle.blackbox.multiplatform.rememberScope
import com.github.trueangle.blackbox.sample.movie.design.TopAppBar
import com.github.trueangle.blackbox.sample.movie.shared.domain.model.Movie
import com.github.trueangle.blackbox.sample.movie.shared.domain.repository.MovieRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Immutable
class TrendingDependencies(val repository: MovieRepository)

sealed interface TrendingOutput {
    data class OnMovieClick(val movie: Movie) : TrendingOutput
}

class TrendingIO : IO<Nothing, TrendingOutput>()

internal data class TrendingSectionState(
    val error: Boolean = false,
    val data: ImmutableList<Movie> = persistentListOf()
)

private class TrendingViewModel(
    private val repository: MovieRepository,
    private val io: TrendingIO
) : ViewModel() {

    val trendingMovies = MutableStateFlow(TrendingSectionState())
    val popularMovies = MutableStateFlow(TrendingSectionState())
    val topRatedMovies = MutableStateFlow(TrendingSectionState())
    val topRatedTVShows = MutableStateFlow(TrendingSectionState())
    val trendingTVShows = MutableStateFlow(TrendingSectionState())

    init {
        loadDataFor(trendingMovies) { repository.getTrending() }
        loadDataFor(popularMovies) { repository.getPopular() }
        loadDataFor(topRatedMovies) { repository.getTopRated() }
        loadDataFor(topRatedTVShows) { repository.getTopRatedTVShows() }
        loadDataFor(trendingTVShows) { repository.getTrendingTVShows() }
    }

    private fun loadDataFor(
        stateFlow: MutableStateFlow<TrendingSectionState>,
        req: suspend () -> List<Movie>
    ) {
        coroutineScope.launch(Dispatchers.IO) {
            runCatching {
                req()
            }.onSuccess {
                stateFlow.emit(
                    stateFlow.value.copy(
                        data = it.toPersistentList(),
                        error = false
                    )
                )
            }.onFailure {
                it.printStackTrace()
                stateFlow.emit(
                    stateFlow.value.copy(
                        error = true
                    )
                )
            }
        }
    }

    fun onMovieSelected(movie: Movie) {
        coroutineScope.launch { io.output(TrendingOutput.OnMovieClick(movie)) }
    }
}

@Composable
fun Trending(modifier: Modifier, dependencies: TrendingDependencies, trendingIO: TrendingIO) {

    val viewModel = rememberScope("TrendingScope") {
        ViewModelScope { TrendingViewModel(dependencies.repository, trendingIO) }
    }.viewModel as TrendingViewModel

    val listOfSections = listOf(
        "Trending this week",
        "Popular this week",
        "Top rated movies",
        "Trending TV shows",
        "Top rated TV shows",
    )

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        TopAppBar("Trending")
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                //.horizontalGradientBackground(surfaceGradient)
                .verticalScroll(rememberScrollState())
        ) {

            listOfSections.forEach { type ->
                val sectionState by when (type) {
                    "Trending this week" -> viewModel.trendingMovies.collectAsState()
                    "Popular this week" -> viewModel.popularMovies.collectAsState()
                    "Trending TV shows" -> viewModel.trendingTVShows.collectAsState()
                    "Top rated movies" -> viewModel.topRatedMovies.collectAsState()
                    "Top rated TV shows" -> viewModel.topRatedTVShows.collectAsState()
                    else -> viewModel.trendingMovies.collectAsState()
                }

                TrendingSection(
                    sectionState = sectionState,
                    title = type,
                    onMovieSelected = viewModel::onMovieSelected
                )
            }
        }
    }

}
