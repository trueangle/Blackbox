package io.github.trueangle.blackbox.sample.movie.shared.ui.home

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import io.github.trueangle.blackbox.sample.movie.shared.ui.featured.FeaturedDependencies
import io.github.trueangle.blackbox.sample.movie.shared.ui.featured.FeaturedIO
import io.github.trueangle.blackbox.sample.movie.shared.ui.trending.TrendingDependencies
import io.github.trueangle.blackbox.sample.movie.shared.ui.trending.TrendingIO
import io.github.trueangle.blackbox.core.IO
import io.github.trueangle.blackbox.multiplatform.Coordinator
import io.github.trueangle.blackbox.multiplatform.FlowScope
import io.github.trueangle.blackbox.sample.movie.shared.domain.model.Movie
import io.github.trueangle.blackbox.sample.movie.shared.domain.repository.GenreRepository
import io.github.trueangle.blackbox.sample.movie.shared.domain.repository.MovieRepository
import io.github.trueangle.blackbox.sample.movie.shared.ui.watchlist.WatchlistDependencies
import io.github.trueangle.blackbox.sample.movie.ticketing.domain.model.Order
import io.github.trueangle.blackbox.sample.movie.ticketing.ui.TicketingFactory

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
    data class OnMovieClick(val movie: Movie, val dominantColors: Pair<Color, Color>) : HomeOutput
    data class OnBuyTicketsClick(val movieName: String) : HomeOutput
}

class HomeIO : IO<HomeInput, HomeOutput>()

internal class HomeScope(
    homeDependencies: HomeDependencies,
    homeIO: HomeIO
) : FlowScope() {

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

    override val coordinator by lazy { HomeCoordinator(homeIO, featuredIO, trendingIO) }
}