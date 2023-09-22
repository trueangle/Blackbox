package com.github.trueangle.blackbox.sample.movie.shared.ui.home

import androidx.compose.runtime.Immutable
import com.github.trueangle.blackbox.sample.movie.shared.ui.featured.FeaturedDependencies
import com.github.trueangle.blackbox.sample.movie.shared.ui.featured.FeaturedIO
import com.github.trueangle.blackbox.sample.movie.shared.ui.trending.TrendingDependencies
import com.github.trueangle.blackbox.sample.movie.shared.ui.trending.TrendingIO
import com.github.trueangle.blackbox.core.IO
import com.github.trueangle.blackbox.multiplatform.FlowScope
import com.github.trueangle.blackbox.sample.movie.shared.domain.model.Movie
import com.github.trueangle.blackbox.sample.movie.shared.domain.repository.GenreRepository
import com.github.trueangle.blackbox.sample.movie.shared.domain.repository.MovieRepository
import com.github.trueangle.blackbox.sample.movie.shared.ui.watchlist.WatchlistDependencies
import com.github.trueangle.blackbox.sample.movie.ticketing.domain.model.Order
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.TicketingFactory

@Immutable
class HomeDependencies(
    val ticketingFactory: TicketingFactory,
    val movieRepository: MovieRepository,
    val genreRepository: GenreRepository
)

sealed interface HomeInput {
    data class OnNewOrder(val order: Order) : HomeInput
}

sealed interface HomeOutput {
    data class OnMovieClick(val movie: Movie) : HomeOutput
    data class OnBuyTicketsClick(val movie: Movie) : HomeOutput
}

class HomeIO : IO<HomeInput, HomeOutput>()

internal class HomeScope(homeDependencies: HomeDependencies, homeIO: HomeIO) : FlowScope() {

    val featuredIO by lazy { FeaturedIO() }
    val trendingIO by lazy { TrendingIO() }

    val featuredModuleDependencies by lazy {
        FeaturedDependencies(
            homeDependencies.movieRepository,
            homeDependencies.genreRepository
        )
    }

    val trendingDependencies by lazy { TrendingDependencies(homeDependencies.movieRepository) }
    val watchlistDependencies by lazy { WatchlistDependencies(homeDependencies.movieRepository) }

    val ticketingFactory = homeDependencies.ticketingFactory

    init {
        coordinator {
            HomeCoordinator(homeIO, featuredIO, trendingIO)
        }
    }
}