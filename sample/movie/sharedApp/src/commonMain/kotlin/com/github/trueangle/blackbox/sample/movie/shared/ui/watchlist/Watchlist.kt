package com.github.trueangle.blackbox.sample.movie.shared.ui.watchlist

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.truangle.blackbox.design.typography
import com.github.trueangle.blackbox.multiplatform.ViewModel
import com.github.trueangle.blackbox.multiplatform.ViewModelScope
import com.github.trueangle.blackbox.multiplatform.rememberScope
import com.github.trueangle.blackbox.multiplatform.rememberViewModel
import com.github.trueangle.blackbox.sample.movie.design.horizontalGradientBackground
import com.github.trueangle.blackbox.sample.movie.design.moviesSurfaceGradient
import com.github.trueangle.blackbox.sample.movie.shared.domain.model.Movie
import com.github.trueangle.blackbox.sample.movie.shared.domain.repository.MovieRepository
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

private class WatchlistViewModel(private val repository: MovieRepository) : ViewModel() {
    val watchlist = MutableStateFlow(persistentListOf<Movie>())

    init {
        coroutineScope.launch {
            repository.getMyWatchList().collect {
                watchlist.value = it.toPersistentList()
            }
        }
    }
}

class WatchlistDependencies(val repository: MovieRepository)

@Composable
fun Watchlist(modifier: Modifier, dependencies: WatchlistDependencies) {

    val viewModel = rememberViewModel {
        WatchlistViewModel(dependencies.repository)
    }

    val myWatchlist by viewModel.watchlist.collectAsState()

    if (myWatchlist.isEmpty()) {
        EmptyWatchlistSection()

        return
    }

    Surface(modifier = modifier) {
        LazyColumn {

            items(myWatchlist) { movie ->
                MovieWatchlistItem(
                    movie,
                    {
                        /*  moviesHomeInteractionEvents(
                              MoviesHomeInteractionEvents.OpenMovieDetail(movie)
                          )*/
                    },
                    {
                        /*   moviesHomeInteractionEvents(
                               MoviesHomeInteractionEvents.RemoveFromMyWatchlist(movie)
                           )*/
                    }
                )
            }
        }
    }
}

@Composable
private fun EmptyWatchlistSection() {
    Column {
        Spacer(modifier = Modifier.padding(100.dp))
        Text(
            text = "Watchlist is empty",
            style = typography.titleLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Please add some movies to your watchlist",
            style = typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}


