package io.github.trueangle.blackbox.sample.movie.shared.ui

import androidx.compose.runtime.Immutable
import io.github.trueangle.blackbox.multiplatform.Coordinator
import io.github.trueangle.blackbox.sample.movie.shared.ui.detail.MovieDetailsDependencies
import io.github.trueangle.blackbox.sample.movie.shared.ui.home.HomeDependencies
import io.github.trueangle.blackbox.sample.movie.shared.ui.home.HomeIO
import io.github.trueangle.blackbox.multiplatform.FlowScope
import io.github.trueangle.blackbox.sample.movie.auth.AuthIO
import io.github.trueangle.blackbox.sample.movie.shared.data.api.MoviesApi
import io.github.trueangle.blackbox.sample.movie.shared.data.repository.GenreRepositoryImpl
import io.github.trueangle.blackbox.sample.movie.shared.data.repository.MovieRepositoryImpl
import io.github.trueangle.blackbox.sample.movie.shared.ui.detail.MovieDetailsIO
import io.github.trueangle.blackbox.sample.movie.ticketing.TicketingFlowIO
import io.github.trueangle.blackbox.sample.movie.ticketing.ui.TicketingDependencies
import io.github.trueangle.blackbox.sample.movie.ticketing.ui.TicketingFactory
import io.ktor.client.HttpClient

@Immutable
class AppDependencies(val httpClient: HttpClient)

internal class AppScope(appDependencies: AppDependencies, config: AppConfig) : FlowScope() {
    val api by lazy { MoviesApi(appDependencies.httpClient) }

    val movieRepository by lazy { MovieRepositoryImpl(api) }
    val genreRepository by lazy { GenreRepositoryImpl(api) }

    val ticketingFlowIO by lazy { TicketingFlowIO() }
    val authIO by lazy { AuthIO() }
    val homeIO by lazy { HomeIO() }
    val movieDetailsIO by lazy { MovieDetailsIO() }

    val movieDetailsDependencies by lazy {
        MovieDetailsDependencies(
            movieRepository,
            genreRepository
        )
    }

    val homeDependencies by lazy {
        HomeDependencies(
            ticketingFactory = ticketingFactory,
            movieRepository = movieRepository,
            genreRepository = genreRepository
        )
    }

    val ticketingFactory by lazy { TicketingFactory(TicketingDependencies(httpClient = appDependencies.httpClient)) }

    override val coordinator by lazy { AppCoordinator(homeIO, ticketingFlowIO, authIO, movieDetailsIO, config) }
}