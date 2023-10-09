package com.github.trueangle.blackbox.sample.movie.shared.ui

import com.github.trueangle.blackbox.multiplatform.Coordinator
import com.github.trueangle.blackbox.sample.movie.auth.AuthFlowOutput
import com.github.trueangle.blackbox.sample.movie.auth.AuthIO
import com.github.trueangle.blackbox.sample.movie.shared.ui.detail.MovieDetailsIO
import com.github.trueangle.blackbox.sample.movie.shared.ui.detail.MovieDetailsOutput
import com.github.trueangle.blackbox.sample.movie.shared.ui.home.HomeIO
import com.github.trueangle.blackbox.sample.movie.shared.ui.home.HomeInput
import com.github.trueangle.blackbox.sample.movie.shared.ui.home.HomeOutput
import com.github.trueangle.blackbox.sample.movie.ticketing.TicketingFlowIO
import com.github.trueangle.blackbox.sample.movie.ticketing.TicketingFlowInput
import com.github.trueangle.blackbox.sample.movie.ticketing.TicketingFlowOutput
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AppCoordinator(
    private val homeIO: HomeIO,
    private val ticketingFlowIO: TicketingFlowIO,
    private val authIO: AuthIO,
    private val movieDetailsIO: MovieDetailsIO,
    private val config: AppConfig
) : Coordinator() {

    init {
        if (config.deeplink != null) {
            navigator.navigateTo(config.deeplink)
        }

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