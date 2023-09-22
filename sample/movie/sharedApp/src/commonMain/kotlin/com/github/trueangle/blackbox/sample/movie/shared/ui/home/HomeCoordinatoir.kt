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
                    HomeRoutes.BottomBar.Orders.RoutePattern,
                    RouteOptions(launchSingleTop = true)
                )
            }
        }.launchIn(coroutineScope)
    }

    fun onBottomNavActionClick(route: HomeRoutes.BottomBar) {
        when (route) {
            HomeRoutes.BottomBar.Featured -> navigator.navigateTo(
                HomeRoutes.BottomBar.Featured.RoutePattern,
                RouteOptions(launchSingleTop = true)
            )

            HomeRoutes.BottomBar.Trending -> navigator.navigateTo(
                HomeRoutes.BottomBar.Trending.RoutePattern,
                RouteOptions(launchSingleTop = true)
            )

            HomeRoutes.BottomBar.Orders -> navigator.navigateTo(
                HomeRoutes.BottomBar.Orders.RoutePattern,
                RouteOptions(launchSingleTop = true)
            )
        }
    }

    private fun handleFeaturedIO() {
        featuredIO.output.onEach {
            when (it) {
                is FeaturedOutput.OnMovieClick -> homeIO.output(HomeOutput.OnMovieClick(it.movie))
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
                is TrendingOutput.OnMovieClick -> homeIO.output(HomeOutput.OnMovieClick(it.movie))
            }
        }.launchIn(coroutineScope)
    }

}