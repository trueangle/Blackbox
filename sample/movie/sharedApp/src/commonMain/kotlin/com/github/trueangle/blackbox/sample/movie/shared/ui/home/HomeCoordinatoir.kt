package com.github.trueangle.blackbox.sample.movie.shared.ui.home

import com.github.trueangle.blackbox.sample.movie.shared.ui.featured.FeaturedIO
import com.github.trueangle.blackbox.sample.movie.shared.ui.featured.FeaturedOutput
import com.github.trueangle.blackbox.sample.movie.shared.ui.trending.TrendingIO
import com.github.trueangle.blackbox.sample.movie.shared.ui.trending.TrendingOutput
import com.github.trueangle.blackbox.core.RouteOptions
import com.github.trueangle.blackbox.multiplatform.Coordinator
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
                HomeRoutes.Featured.routePattern,
                RouteOptions(
                    popUpTo = RouteOptions.PopUpTo(
                        inclusive = true,
                        route = HomeRoutes.Featured.routePattern
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
                        it.movie
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