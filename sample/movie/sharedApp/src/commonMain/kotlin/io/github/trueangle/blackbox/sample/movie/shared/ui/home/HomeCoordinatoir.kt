package io.github.trueangle.blackbox.sample.movie.shared.ui.home

import io.github.trueangle.blackbox.sample.movie.shared.ui.featured.FeaturedIO
import io.github.trueangle.blackbox.sample.movie.shared.ui.featured.FeaturedOutput
import io.github.trueangle.blackbox.sample.movie.shared.ui.trending.TrendingIO
import io.github.trueangle.blackbox.sample.movie.shared.ui.trending.TrendingOutput
import io.github.trueangle.blackbox.core.RouteOptions
import io.github.trueangle.blackbox.multiplatform.Coordinator
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class HomeCoordinator(
    private val homeIO: HomeIO,
    private val featuredIO: FeaturedIO,
    private val trendingIO: TrendingIO,
) : Coordinator() {

    init {
        handleInput()
        handleFeaturedIO()
        handleTrendingIO()
    }

    private fun handleInput() {
        homeIO.input.onEach {
            when (it) {
                is HomeInput.OnNewOrder -> navigator.navigateTo(
                    HomeRoutes.Orders.routePattern,
                    RouteOptions(launchSingleTop = true)
                )
            }
        }.launchIn(coroutineScope)
    }

    fun onBottomNavActionClick(clickedTabRoute: HomeRoutes) {
        when (clickedTabRoute) {
            HomeRoutes.Featured -> navigator.navigateTo(
                route = HomeRoutes.Featured.routePattern,
                routeOptions = RouteOptions(
                    popUpTo = RouteOptions.PopUpTo(
                        route = HomeRoutes.Featured.routePattern,
                        inclusive = true
                    )
                )
            )

            HomeRoutes.Trending -> navigator.navigateTo(
                HomeRoutes.Trending.routePattern,
                RouteOptions(
                    launchSingleTop = true,
                    popUpTo = RouteOptions.PopUpTo(
                        route = HomeRoutes.Featured.routePattern
                    )
                )
            )

            HomeRoutes.Orders -> navigator.navigateTo(
                HomeRoutes.Orders.routePattern,
                RouteOptions(
                    launchSingleTop = true,
                    popUpTo = RouteOptions.PopUpTo(
                        HomeRoutes.Featured.routePattern
                    )
                )
            )
        }
    }

    private fun handleFeaturedIO() {
        featuredIO.output.onEach {
            when (it) {
                is FeaturedOutput.OnMovieClick -> homeIO.output(
                    HomeOutput.OnMovieClick(
                        it.movie,
                        it.dominantColors
                    )
                )

                is FeaturedOutput.OnBuyTicketClick -> homeIO.output(
                    HomeOutput.OnBuyTicketsClick(
                        it.movie.title ?: it.movie.name ?: ""
                    )
                )
            }
        }.launchIn(coroutineScope)
    }

    private fun handleTrendingIO() {
        trendingIO.output.onEach {
            when (it) {
                is TrendingOutput.OnMovieClick -> homeIO.output(
                    HomeOutput.OnMovieClick(
                        it.movie,
                        it.dominantColors
                    )
                )
            }
        }.launchIn(coroutineScope)
    }
}