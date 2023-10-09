package com.github.trueangle.blackbox.sample.movie.shared.ui

import androidx.compose.runtime.Immutable
import com.github.trueangle.blackbox.sample.movie.shared.ui.detail.MovieDetailsDependencies
import com.github.trueangle.blackbox.sample.movie.shared.ui.home.HomeDependencies
import com.github.trueangle.blackbox.sample.movie.shared.ui.home.HomeIO
import com.github.trueangle.blackbox.multiplatform.FlowScope
import com.github.trueangle.blackbox.sample.movie.auth.AuthIO
import com.github.trueangle.blackbox.sample.movie.shared.data.api.MoviesApi
import com.github.trueangle.blackbox.sample.movie.shared.data.repository.GenreRepositoryImpl
import com.github.trueangle.blackbox.sample.movie.shared.data.repository.MovieRepositoryImpl
import com.github.trueangle.blackbox.sample.movie.shared.ui.detail.MovieDetailsIO
import com.github.trueangle.blackbox.sample.movie.ticketing.TicketingFlowIO
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.TicketingDependencies
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.TicketingFactory
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

    init {
        coordinator { AppCoordinator(homeIO, ticketingFlowIO, authIO, movieDetailsIO, config) }
    }
}