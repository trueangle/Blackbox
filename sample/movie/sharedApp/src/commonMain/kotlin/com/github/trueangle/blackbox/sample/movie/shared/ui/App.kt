package com.github.trueangle.blackbox.sample.movie.shared.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.trueangle.blackbox.multiplatform.NavigationFlow
import com.github.trueangle.blackbox.multiplatform.rememberScope
import com.github.trueangle.blackbox.sample.movie.auth.AuthFlow
import com.github.trueangle.blackbox.sample.movie.design.MovieAppTheme
import com.github.trueangle.blackbox.sample.movie.shared.ui.detail.MovieDetails
import com.github.trueangle.blackbox.sample.movie.shared.ui.detail.MovieDetailsConfig
import com.github.trueangle.blackbox.sample.movie.shared.ui.home.Home
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.TicketingConfig
import moe.tlaster.precompose.navigation.path

@Immutable
data class AppConfig(val deeplink: String? = null)

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
            const val RoutePattern: String = "movie://app/ticketing/{movieName}"

            fun routeWithParam(movieName: String) = "movie://app/ticketing/${movieName}"
        }
    }

    data object AuthFlow : AppRoutes {
        const val RoutePattern: String = "auth"
    }
}

@Composable
fun App(appDependencies: AppDependencies, config: AppConfig) {
    val appScope = rememberScope { AppScope(appDependencies, config) }

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