package com.github.trueangle.blackbox.sample.movie.shared.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.truangle.blackbox.movieapp.ui.detail.MovieDetails
import com.github.trueangle.blackbox.sample.movie.shared.ui.home.Home
import com.github.trueangle.blackbox.sample.movie.shared.ui.home.HomeIO
import com.github.trueangle.blackbox.sample.movie.shared.ui.home.HomeInput
import com.github.trueangle.blackbox.sample.movie.shared.ui.home.HomeOutput
import com.github.trueangle.blackbox.multiplatform.Coordinator
import com.github.trueangle.blackbox.multiplatform.NavigationFlow
import com.github.trueangle.blackbox.multiplatform.rememberScope
import com.github.trueangle.blackbox.sample.movie.auth.AuthFlow
import com.github.trueangle.blackbox.sample.movie.auth.AuthFlowOutput
import com.github.trueangle.blackbox.sample.movie.auth.AuthIO
import com.github.trueangle.blackbox.sample.movie.design.MovieAppTheme
import com.github.trueangle.blackbox.sample.movie.shared.domain.model.Movie
import com.github.trueangle.blackbox.sample.movie.ticketing.TicketingFlowIO
import com.github.trueangle.blackbox.sample.movie.ticketing.TicketingFlowInput
import com.github.trueangle.blackbox.sample.movie.ticketing.TicketingFlowOutput
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.path

sealed interface AppRoutes {
    data object Home : AppRoutes {
        const val RoutePattern: String = "home"
    }

    data object MovieDetails : AppRoutes {
        const val RoutePattern: String = "movieDetails/{movieId}"

        fun routeWithParam(movieId: String) = "movieDetails/${movieId}"
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
    val appScope = rememberScope("AppScope") { AppScope(appDependencies) }

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
                    movieId = requireNotNull(entry.path<String>("movieId")),
                    dependencies = appScope.movieDetailsDependencies
                )
            }

            dialog(route = AppRoutes.Ticketing.Flow.RoutePattern) { entry ->
                appScope.ticketingFactory.TicketingFlow(
                    io = appScope.ticketingFlowIO,
                    movieName = requireNotNull(entry.path<String>("movieName"))
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

// Temporary abstraction level until proper Modal Window navigation is supported by nav library which in turn
// waits for Compose Multiplatform modal window insets support to be released
class AppCoordinator(
    private val homeIO: HomeIO,
    private val ticketingFlowIO: TicketingFlowIO,
    private val authIO: AuthIO
) : Coordinator() {

    init {
        handleHomeIO()
        handleTicketingFlowIO()
        handleAuthIO()
    }

    private fun handleHomeIO() {
        coroutineScope.launch {
            homeIO.output.collect {
                when (it) {
                    is HomeOutput.OnBuyTicketsClick -> navigateToTicketingFlow(it.movie)
                    is HomeOutput.OnMovieClick -> navigateToMovieDetails(it.movie)
                }
            }
        }
    }

    private fun handleTicketingFlowIO() {
        coroutineScope.launch {
            ticketingFlowIO.output.collect {
                when (it) {
                    is TicketingFlowOutput.OnClose -> navigator.back()
                    TicketingFlowOutput.OnRequestAuth ->
                        navigator.navigateTo(AppRoutes.AuthFlow.RoutePattern)

                    is TicketingFlowOutput.OnPurchased -> {
                        homeIO.input(HomeInput.OnNewOrder(it.order))
                        navigator.back()
                    }
                }
            }
        }
    }

    private fun navigateToTicketingFlow(movie: Movie) {
        navigator.navigateTo(
            AppRoutes.Ticketing.Flow.routeWithParam(
                movie.title ?: movie.name ?: "null"
            )
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

    private fun navigateToMovieDetails(movie: Movie) {
        navigator.navigateTo(
            AppRoutes.MovieDetails.routeWithParam(movie.id)
        )
    }
}