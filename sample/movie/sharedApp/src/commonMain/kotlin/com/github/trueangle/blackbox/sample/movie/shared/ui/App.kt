package com.github.trueangle.blackbox.sample.movie.shared.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.trueangle.blackbox.multiplatform.Coordinator
import com.github.trueangle.blackbox.multiplatform.NavigationFlow
import com.github.trueangle.blackbox.multiplatform.rememberScope
import com.github.trueangle.blackbox.sample.movie.auth.AuthFlow
import com.github.trueangle.blackbox.sample.movie.auth.AuthFlowOutput
import com.github.trueangle.blackbox.sample.movie.auth.AuthIO
import com.github.trueangle.blackbox.sample.movie.design.MovieAppTheme
import com.github.trueangle.blackbox.sample.movie.shared.ui.detail.MovieDetails
import com.github.trueangle.blackbox.sample.movie.shared.ui.detail.MovieDetailsConfig
import com.github.trueangle.blackbox.sample.movie.shared.ui.detail.MovieDetailsIO
import com.github.trueangle.blackbox.sample.movie.shared.ui.detail.MovieDetailsOutput
import com.github.trueangle.blackbox.sample.movie.shared.ui.home.Home
import com.github.trueangle.blackbox.sample.movie.shared.ui.home.HomeIO
import com.github.trueangle.blackbox.sample.movie.shared.ui.home.HomeInput
import com.github.trueangle.blackbox.sample.movie.shared.ui.home.HomeOutput
import com.github.trueangle.blackbox.sample.movie.ticketing.TicketingFlowIO
import com.github.trueangle.blackbox.sample.movie.ticketing.TicketingFlowInput
import com.github.trueangle.blackbox.sample.movie.ticketing.TicketingFlowOutput
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.TicketingConfig
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.path

sealed interface AppRoutes {
    data object Home : AppRoutes {
        const val RoutePattern: String = "home"
    }

    data object MovieDetails : AppRoutes {
        const val RoutePattern: String =
            "movieDetails/{movieId}/{movieName}/{domColor}/{domOnColor}"

        fun routeWithParam(movieId: String, movieName: String, dominantColors: Pair<Color, Color>) =
            "movieDetails/${movieId}/${movieName}/${dominantColors.first.value}/${dominantColors.second.value}"
    }

    sealed interface Ticketing : AppRoutes {
        data object Flow : Ticketing {
            const val RoutePattern: String = "ticketing/{movieName}"

            fun routeWithParam(movieName: String) = "ticketing/${movieName}"
        }
    }

    data object AuthFlow : AppRoutes {
        const val RoutePattern: String = "auth"
    }
}

@Composable
fun App(appDependencies: AppDependencies) {
    val appScope = rememberScope { AppScope(appDependencies) }

    MovieAppTheme {
        NavigationFlow(
            modifier = Modifier.fillMaxSize(),
            coordinator = appScope.coordinator,
            startDestination = AppRoutes.Home.RoutePattern
        ) {

            scene(route = AppRoutes.Home.RoutePattern) {
                Home(
                    modifier = Modifier.fillMaxSize(),
                    dependencies = appScope.homeDependencies,
                    homeIO = appScope.homeIO
                )
            }

            dialog(route = AppRoutes.MovieDetails.RoutePattern) { entry ->
                MovieDetails(
                    modifier = Modifier.fillMaxSize(),
                    MovieDetailsConfig(
                        movieId = requireNotNull(entry.path<String>("movieId")),
                        movieName = requireNotNull(entry.path<String>("movieName")),
                        dominantColor = requireNotNull(entry.path<String>("domColor")),
                        dominantOnColor = requireNotNull(entry.path<String>("domOnColor"))
                    ),
                    dependencies = appScope.movieDetailsDependencies,
                    io = appScope.movieDetailsIO
                )
            }

            dialog(route = AppRoutes.Ticketing.Flow.RoutePattern) { entry ->
                appScope.ticketingFactory.CreateTicketingFlow(
                    modifier = Modifier.fillMaxSize(),
                    io = appScope.ticketingFlowIO,
                    config = TicketingConfig(
                        movieName = requireNotNull(entry.path<String>("movieName"))
                    )
                )
            }

            dialog(route = AppRoutes.AuthFlow.RoutePattern) {
                AuthFlow(
                    modifier = Modifier.fillMaxSize(),
                    io = appScope.authIO
                )
            }
        }
    }
}

class AppCoordinator(
    private val homeIO: HomeIO,
    private val ticketingFlowIO: TicketingFlowIO,
    private val authIO: AuthIO,
    private val movieDetailsIO: MovieDetailsIO
) : Coordinator() {

    init {
        handleHomeIO()
        handleTicketingFlowIO()
        handleAuthIO()
        handleMovieDetailsIO()
    }

    private fun handleMovieDetailsIO() {
        movieDetailsIO.output.onEach {
            when (it) {
                MovieDetailsOutput.OnClose -> navigator.back()
                is MovieDetailsOutput.OnRequestTickets -> navigateToTicketingFlow(it.movieName)
            }
        }.launchIn(coroutineScope)
    }

    private fun handleHomeIO() {
        homeIO.output.onEach {
            when (it) {
                is HomeOutput.OnBuyTicketsClick -> navigateToTicketingFlow(it.movieName)
                is HomeOutput.OnMovieClick -> navigateToMovieDetails(it)
            }
        }.launchIn(coroutineScope)
    }

    private fun handleTicketingFlowIO() {
        ticketingFlowIO.output.onEach {
            when (it) {
                is TicketingFlowOutput.OnClose -> navigator.back()
                TicketingFlowOutput.OnRequestAuth ->
                    navigator.navigateTo(AppRoutes.AuthFlow.RoutePattern)

                is TicketingFlowOutput.OnPurchased -> {
                    homeIO.input(HomeInput.OnNewOrder(it.order))
                    navigator.back()
                }
            }
        }.launchIn(coroutineScope)
    }

    private fun navigateToTicketingFlow(movieName: String) {
        navigator.navigateTo(
            AppRoutes.Ticketing.Flow.routeWithParam(movieName)
        )
    }

    private fun handleAuthIO() {
        coroutineScope.launch {
            authIO.output.collect {
                when (it) {
                    AuthFlowOutput.OnClose -> navigator.back()
                    is AuthFlowOutput.OnPickUserData -> {
                        ticketingFlowIO.input(TicketingFlowInput.OnUserAuthorized(it.user))
                        navigator.back()
                    }
                }
            }
        }
    }

    private fun navigateToMovieDetails(event: HomeOutput.OnMovieClick) {
        navigator.navigateTo(
            AppRoutes.MovieDetails.routeWithParam(
                movieId = event.movie.id,
                movieName = event.movie.title ?: event.movie.name ?: "",
                dominantColors = event.dominantColors
            )
        )
    }
}