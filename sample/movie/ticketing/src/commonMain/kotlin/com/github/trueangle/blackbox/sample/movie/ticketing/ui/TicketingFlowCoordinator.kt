package com.github.trueangle.blackbox.sample.movie.ticketing.ui

import com.github.trueangle.blackbox.multiplatform.Coordinator
import com.github.trueangle.blackbox.sample.movie.ticketing.FlowRoutes
import com.github.trueangle.blackbox.sample.movie.ticketing.TicketingFlowIO
import com.github.trueangle.blackbox.sample.movie.ticketing.TicketingFlowInput
import com.github.trueangle.blackbox.sample.movie.ticketing.TicketingFlowOutput
import com.github.trueangle.blackbox.sample.movie.ticketing.domain.model.Cinema
import com.github.trueangle.blackbox.sample.movie.ticketing.domain.model.Seat
import com.github.trueangle.blackbox.sample.movie.ticketing.domain.model.ShowTime
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.cinema.CinemaListIO
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.cinema.CinemaListOutput
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.seats.SeatsIO
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.seats.SeatsOutput
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.summary.TicketSummaryIO
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.summary.TicketSummaryInput
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.summary.TicketSummaryOutput
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

internal data class FlowState(
    val movieName: String,
    val selectedCinema: Cinema? = null,
    val selectedShowTime: ShowTime? = null,
    val selectedSeats: ImmutableList<Seat> = persistentListOf()
)

internal class TicketingFlowCoordinator(
    private val movieName: String,
    private val flowIO: TicketingFlowIO,
    private val cinemaListIO: CinemaListIO,
    private val seatsIO: SeatsIO,
    private val ticketSummaryIO: TicketSummaryIO
) : Coordinator() {

    var flowState = FlowState(movieName)

    init {
        handleFlowIO()

        handleCinemaListIO()
        handleSeatsIO()
        handleSummaryIO()
    }

    fun onToolbarCloseClick() {
        coroutineScope.launch {
            flowIO.output(TicketingFlowOutput.OnClose)
        }
    }

    private fun handleFlowIO() {
        coroutineScope.launch {
            flowIO.input.collect {
                when (it) {
                    is TicketingFlowInput.OnUserAuthorized -> ticketSummaryIO.input(
                        TicketSummaryInput.OnUserData(it.user)
                    )
                }
            }
        }
    }

    private fun handleSeatsIO() {
        coroutineScope.launch {
            seatsIO.output.collect {
                when (it) {
                    is SeatsOutput.OnFinish -> {
                        flowState = flowState.copy(
                            selectedSeats = it.seats
                        )

                        navigator.navigateTo(FlowRoutes.Summary.RoutePattern)
                    }
                }
            }
        }
    }

    private fun handleCinemaListIO() {
        coroutineScope.launch {
            cinemaListIO.output.collect {
                when (it) {
                    is CinemaListOutput.OnShowTimeSelected -> {
                        flowState = flowState.copy(
                            selectedCinema = it.selectedCinema,
                            selectedShowTime = it.selectedShowTime
                        )

                        navigator.navigateTo(FlowRoutes.Seats.RoutePattern)
                    }
                }
            }
        }
    }

    private fun handleSummaryIO() {
        coroutineScope.launch {
            ticketSummaryIO.output.collect {
                when (it) {
                    TicketSummaryOutput.OnRequestUserData -> flowIO.output(TicketingFlowOutput.OnRequestAuth)
                    is TicketSummaryOutput.OnPurchased -> flowIO.output(
                        TicketingFlowOutput.OnPurchased(
                            it.order
                        )
                    )
                }
            }
        }
    }
}